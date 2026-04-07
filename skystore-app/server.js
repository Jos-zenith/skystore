const express = require('express');
const cors = require('cors');
const fs = require('fs/promises');
const path = require('path');
const { MongoClient } = require('mongodb');

const app = express();
const port = Number(process.env.APP_PORT || 3000);
const cloudStatus = process.env.CLOUD_STATUS || 'AWS: Connected';
const sessionUser = process.env.SESSION_USER || 'admin@skystore.io';
const sessionPassword = process.env.SESSION_PASSWORD || 'Password123!';
const databaseFile = process.env.DATABASE_FILE || path.join(__dirname, 'data', 'products.json');
const mongoUri = process.env.MONGODB_URI || '';
const mongoDatabaseName = process.env.MONGODB_DB || 'skystore';
const publicDir = path.join(__dirname, 'public');

app.use(cors());
app.use(express.json());
app.use(express.static(publicDir));

let activityLog = [];
let mongoClient = null;
let productsCollection = null;
let logsCollection = null;
let persistenceMode = 'file';

async function ensureStore() {
  try {
    await fs.access(databaseFile);
  } catch {
    await fs.mkdir(path.dirname(databaseFile), { recursive: true });
    await fs.writeFile(databaseFile, '[]', 'utf8');
  }
}

async function initializePersistence() {
  if (!mongoUri.trim()) {
    await ensureStore();
    persistenceMode = 'file';
    return;
  }

  mongoClient = new MongoClient(mongoUri);
  await mongoClient.connect();
  const database = mongoClient.db(mongoDatabaseName);
  productsCollection = database.collection('products');
  logsCollection = database.collection('activityLogs');
  await productsCollection.createIndex({ sku: 1 }, { unique: true });
  await logsCollection.createIndex({ timestamp: -1 });
  persistenceMode = 'mongodb';
}

async function readProducts() {
  if (persistenceMode === 'mongodb') {
    return productsCollection.find({}).sort({ createdAt: -1 }).toArray();
  }

  await ensureStore();
  const raw = await fs.readFile(databaseFile, 'utf8');
  const products = JSON.parse(raw || '[]');
  return Array.isArray(products) ? products : [];
}

async function writeProducts(products) {
  if (persistenceMode === 'mongodb') {
    await productsCollection.deleteMany({});
    if (products.length > 0) {
      await productsCollection.insertMany(products.map((product) => ({ ...product })));
    }
    return;
  }

  await ensureStore();
  await fs.writeFile(databaseFile, JSON.stringify(products, null, 2), 'utf8');
}

async function recordEvent(message, level = 'info') {
  const event = {
    id: `${Date.now()}-${Math.random().toString(16).slice(2)}`,
    message,
    level,
    timestamp: new Date().toISOString()
  };

  if (persistenceMode === 'mongodb') {
    await logsCollection.insertOne(event);
    return;
  }

  activityLog.unshift(event);
  activityLog = activityLog.slice(0, 25);
}

async function readLogs() {
  if (persistenceMode === 'mongodb') {
    return logsCollection.find({}).sort({ timestamp: -1 }).limit(25).toArray();
  }

  return activityLog;
}

app.get('/api/health', (_req, res) => {
  res.json({
    status: 'ok',
    cloudStatus,
    databaseFile,
    persistenceMode,
    timestamp: new Date().toISOString()
  });
});

app.post('/api/auth/login', (req, res) => {
  const { email, password } = req.body || {};
  if (email === sessionUser && password === sessionPassword) {
    recordEvent(`User ${email} signed in`).catch(() => null);
    return res.json({
      user: {
        email,
        name: 'SkyStore Admin'
      },
      token: 'demo-session-token'
    });
  }

  return res.status(401).json({ message: 'Invalid credentials' });
});

app.get('/api/products', async (_req, res) => {
  const products = await readProducts();
  res.json(products);
});

app.post('/api/products', async (req, res) => {
  const { name, price, sku, category } = req.body || {};
  if (!name || !sku || !category || price === undefined || price === null) {
    return res.status(400).json({ message: 'Missing required product fields' });
  }

  const products = await readProducts();
  if (products.some((product) => product.sku === sku)) {
    return res.status(409).json({ message: 'SKU already exists' });
  }

  const nextProduct = {
    name: String(name).trim(),
    price: Number(price),
    sku: String(sku).trim(),
    category: String(category).trim(),
    createdAt: new Date().toISOString()
  };

  products.unshift(nextProduct);
  await writeProducts(products);
  await recordEvent(`Product ${nextProduct.sku} created`);
  res.status(201).json(nextProduct);
});

app.put('/api/products/:sku', async (req, res) => {
  const { sku } = req.params;
  const { name, price, category } = req.body || {};
  const products = await readProducts();
  const index = products.findIndex((product) => product.sku === sku);

  if (index === -1) {
    return res.status(404).json({ message: 'Product not found' });
  }

  products[index] = {
    ...products[index],
    name: name !== undefined ? String(name).trim() : products[index].name,
    price: price !== undefined ? Number(price) : products[index].price,
    category: category !== undefined ? String(category).trim() : products[index].category
  };

  await writeProducts(products);
  await recordEvent(`Product ${sku} updated`);
  res.json(products[index]);
});

app.delete('/api/products/:sku', async (req, res) => {
  const { sku } = req.params;
  const products = await readProducts();
  const filtered = products.filter((product) => product.sku !== sku);

  if (filtered.length === products.length) {
    return res.status(404).json({ message: 'Product not found' });
  }

  await writeProducts(filtered);
  await recordEvent(`Product ${sku} deleted`, 'warning');
  res.status(204).end();
});

app.get('/api/logs', async (_req, res) => {
  const logs = await readLogs();
  res.json(logs);
});

app.get('*', (_req, res) => {
  res.sendFile(path.join(publicDir, 'index.html'));
});

ensureStore().then(() => {
  initializePersistence()
    .catch((error) => {
      console.warn('MongoDB connection failed, falling back to file storage:', error.message);
      persistenceMode = 'file';
      return ensureStore();
    })
    .finally(() => {
      app.listen(port, () => {
        console.log(`SkyStore app listening on http://localhost:${port}`);
      });
    });
});
