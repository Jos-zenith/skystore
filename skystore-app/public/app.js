const state = {
  user: null,
  products: [],
  filteredProducts: [],
  logs: [],
  currentView: 'dashboard',
  editingSku: null,
  searchTerm: ''
};

const elements = {
  loginScreen: document.getElementById('login-screen'),
  appShell: document.getElementById('app-shell'),
  loginForm: document.getElementById('login-form'),
  loginEmail: document.getElementById('login-email'),
  loginPassword: document.getElementById('login-password'),
  sessionUserLabel: document.getElementById('session-user-label'),
  logoutButton: document.getElementById('logout-button'),
  cloudStatusBadge: document.getElementById('cloud-status-badge'),
  pageTitle: document.getElementById('page-title'),
  breadcrumbRow: document.getElementById('breadcrumb-row'),
  globalSearchInput: document.getElementById('global-search-input'),
  regionSelector: document.getElementById('region-selector'),
  totalProducts: document.getElementById('total-products'),
  healthStatus: document.getElementById('health-status'),
  latestSku: document.getElementById('latest-sku'),
  productsTbody: document.getElementById('products-tbody'),
  logsList: document.getElementById('logs-list'),
  settingsCloudStatus: document.getElementById('settings-cloud-status'),
  settingsDatabase: document.getElementById('settings-database'),
  settingsPersistence: document.getElementById('settings-persistence'),
  addProductButton: document.getElementById('add-product-btn'),
  productModal: document.getElementById('product-modal'),
  closeModalButton: document.getElementById('close-modal-button'),
  modalTitle: document.getElementById('modal-title'),
  productForm: document.getElementById('product-form'),
  productNameInput: document.getElementById('product-name-input'),
  productSkuInput: document.getElementById('product-sku-input'),
  productCategoryInput: document.getElementById('product-category-input'),
  productPriceInput: document.getElementById('product-price-input'),
  toast: document.getElementById('toast'),
  navLinks: Array.from(document.querySelectorAll('.nav-link'))
};

const viewMeta = {
  dashboard: { title: 'Dashboard', breadcrumb: 'Services > Dashboard' },
  inventory: { title: 'Inventory', breadcrumb: 'Services > Inventory > Product Catalog' },
  'bulk-upload': { title: 'Bulk Upload', breadcrumb: 'Services > Inventory > Bulk Upload' },
  logs: { title: 'User Logs', breadcrumb: 'Services > Logs > User Activity' },
  settings: { title: 'Cloud Settings', breadcrumb: 'Services > Settings > Cloud Configuration' }
};

function setScreen(isAuthenticated) {
  elements.loginScreen.classList.toggle('hidden', isAuthenticated);
  elements.appShell.classList.toggle('hidden', !isAuthenticated);
}

function setView(view) {
  state.currentView = view;
  document.querySelectorAll('.view-panel').forEach((panel) => panel.classList.add('hidden'));
  document.getElementById(`${view}-view`).classList.remove('hidden');
  const meta = viewMeta[view] || { title: view, breadcrumb: `Services > ${view}` };
  elements.pageTitle.textContent = meta.title;
  elements.breadcrumbRow.textContent = meta.breadcrumb;
  elements.navLinks.forEach((link) => link.classList.toggle('active', link.dataset.view === view));
}

function showToast(message, level = 'success') {
  elements.toast.textContent = message;
  elements.toast.style.background = level === 'warning' ? 'rgba(217, 119, 6, 0.96)' : 'rgba(15, 157, 88, 0.96)';
  elements.toast.classList.remove('hidden');
  window.clearTimeout(showToast.timer);
  showToast.timer = window.setTimeout(() => elements.toast.classList.add('hidden'), 2200);
}

function openModal(mode = 'create', product = null) {
  state.editingSku = product ? product.sku : null;
  elements.modalTitle.textContent = mode === 'edit' ? 'Edit product' : 'Add product';
  elements.productNameInput.value = product?.name || '';
  elements.productSkuInput.value = product?.sku || '';
  elements.productSkuInput.disabled = Boolean(product);
  elements.productCategoryInput.value = product?.category || '';
  elements.productPriceInput.value = product?.price ?? '';
  elements.productModal.classList.remove('hidden');
  elements.productModal.setAttribute('aria-hidden', 'false');
}

function closeModal() {
  elements.productModal.classList.add('hidden');
  elements.productModal.setAttribute('aria-hidden', 'true');
  elements.productForm.reset();
  elements.productSkuInput.disabled = false;
  state.editingSku = null;
}

async function requestJson(url, options = {}) {
  const response = await fetch(url, {
    headers: {
      'Content-Type': 'application/json'
    },
    ...options
  });

  if (!response.ok) {
    const payload = await response.json().catch(() => ({}));
    throw new Error(payload.message || 'Request failed');
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}

async function loadHealth() {
  const health = await requestJson('/api/health');
  elements.cloudStatusBadge.textContent = health.cloudStatus;
  elements.healthStatus.textContent = health.status.toUpperCase();
  elements.settingsCloudStatus.textContent = health.cloudStatus;
  elements.settingsDatabase.textContent = health.databaseFile;
  elements.settingsPersistence.textContent = health.persistenceMode || 'file';
}

function renderDashboard() {
  elements.totalProducts.textContent = String(state.filteredProducts.length);
  elements.latestSku.textContent = state.filteredProducts[0]?.sku || '--';
}

function renderProducts() {
  elements.productsTbody.innerHTML = state.filteredProducts
    .map(
      (product) => `
        <tr data-testid="product-row-${product.sku}">
          <td>${product.name}</td>
          <td>${product.sku}</td>
          <td>${product.category}</td>
          <td>$${Number(product.price).toFixed(2)}</td>
          <td>
            <div class="row-actions">
              <button class="action-button" data-action="edit" data-sku="${product.sku}">Edit</button>
              <button class="action-button danger" data-action="delete" data-sku="${product.sku}">Delete</button>
            </div>
          </td>
        </tr>
      `
    )
    .join('');
}

function renderLogs() {
  const term = state.searchTerm.toLowerCase();
  const filteredLogs = term
    ? state.logs.filter((entry) => `${entry.message} ${entry.timestamp}`.toLowerCase().includes(term))
    : state.logs;

  elements.logsList.innerHTML = filteredLogs
    .map(
      (entry) => `
        <li>
          <strong>${entry.message}</strong>
          <div class="muted">${new Date(entry.timestamp).toLocaleString()}</div>
        </li>
      `
    )
    .join('');
}

async function loadData() {
  const [products, logs] = await Promise.all([requestJson('/api/products'), requestJson('/api/logs')]);
  state.products = products;
  state.filteredProducts = products;
  state.logs = logs;
  applySearchFilter();
}

function applySearchFilter() {
  const term = state.searchTerm.toLowerCase();
  state.filteredProducts = term
    ? state.products.filter((product) => {
      const joined = `${product.name} ${product.sku} ${product.category}`.toLowerCase();
      return joined.includes(term);
    })
    : [...state.products];

  renderDashboard();
  renderProducts();
  renderLogs();
}

async function handleLogin(event) {
  event.preventDefault();
  const email = elements.loginEmail.value.trim();
  const password = elements.loginPassword.value;

  const session = await requestJson('/api/auth/login', {
    method: 'POST',
    body: JSON.stringify({ email, password })
  });

  window.localStorage.setItem('skystore-session', JSON.stringify(session.user));
  state.user = session.user;
  elements.sessionUserLabel.textContent = session.user.email;
  setScreen(true);
  setView('dashboard');
  await Promise.all([loadHealth(), loadData()]);
  showToast('Signed in successfully');
}

async function saveProduct(event) {
  event.preventDefault();
  const payload = {
    name: elements.productNameInput.value.trim(),
    sku: elements.productSkuInput.value.trim(),
    category: elements.productCategoryInput.value.trim(),
    price: Number(elements.productPriceInput.value)
  };

  if (state.editingSku) {
    await requestJson(`/api/products/${state.editingSku}`, {
      method: 'PUT',
      body: JSON.stringify({
        name: payload.name,
        category: payload.category,
        price: payload.price
      })
    });
    showToast('Product updated successfully');
  } else {
    await requestJson('/api/products', {
      method: 'POST',
      body: JSON.stringify(payload)
    });
    showToast('Product saved successfully');
  }

  closeModal();
  await Promise.all([loadHealth(), loadData()]);
}

async function deleteProduct(sku) {
  await requestJson(`/api/products/${encodeURIComponent(sku)}`, { method: 'DELETE' });
  showToast(`Deleted ${sku}`, 'warning');
  await Promise.all([loadHealth(), loadData()]);
}

function bindEvents() {
  elements.loginForm.addEventListener('submit', (event) => {
    handleLogin(event).catch((error) => showToast(error.message, 'warning'));
  });

  elements.logoutButton.addEventListener('click', () => {
    window.localStorage.removeItem('skystore-session');
    state.user = null;
    setScreen(false);
    elements.sessionUserLabel.textContent = 'Not signed in';
    showToast('Signed out');
  });

  elements.navLinks.forEach((button) => {
    button.addEventListener('click', () => {
      setView(button.dataset.view);
    });
  });

  elements.globalSearchInput.addEventListener('input', (event) => {
    state.searchTerm = event.target.value.trim();
    applySearchFilter();
  });

  elements.regionSelector.addEventListener('change', (event) => {
    showToast(`Region switched to ${event.target.value}`);
  });

  elements.addProductButton.addEventListener('click', () => openModal('create'));
  elements.closeModalButton.addEventListener('click', closeModal);
  elements.productModal.addEventListener('click', (event) => {
    if (event.target === elements.productModal) {
      closeModal();
    }
  });
  elements.productForm.addEventListener('submit', (event) => {
    saveProduct(event).catch((error) => showToast(error.message, 'warning'));
  });

  elements.productsTbody.addEventListener('click', async (event) => {
    const button = event.target.closest('button[data-action]');
    if (!button) {
      return;
    }

    const product = state.products.find((entry) => entry.sku === button.dataset.sku);
    if (!product) {
      return;
    }

    if (button.dataset.action === 'edit') {
      openModal('edit', product);
    }

    if (button.dataset.action === 'delete') {
      try {
        await deleteProduct(product.sku);
      } catch (error) {
        showToast(error.message, 'warning');
      }
    }
  });
}

async function restoreSession() {
  const stored = window.localStorage.getItem('skystore-session');
  if (!stored) {
    return;
  }

  try {
    const user = JSON.parse(stored);
    state.user = user;
    elements.sessionUserLabel.textContent = user.email;
    setScreen(true);
    setView('dashboard');
    await Promise.all([loadHealth(), loadData()]);
  } catch {
    window.localStorage.removeItem('skystore-session');
  }
}

async function boot() {
  bindEvents();
  await restoreSession();
  if (!state.user) {
    await loadHealth();
    setView('dashboard');
    setScreen(false);
  }
}

boot().catch((error) => {
  showToast(error.message, 'warning');
  console.error(error);
});
