param(
    [string]$OutputPath = (Join-Path $PSScriptRoot '..\src\test\resources\data\products.xlsx')
)

$ErrorActionPreference = 'Stop'

$products = @(
    @{ Name = 'Cloud Server Pro'; Price = 249.99; SKU = 'SKU-CLOUD-PRO-01'; Category = 'Infrastructure' },
    @{ Name = 'Edge Gateway'; Price = 149.50; SKU = 'SKU-EDGE-GW-02'; Category = 'Networking' },
    @{ Name = 'Observability Bundle'; Price = 89.00; SKU = 'SKU-OBS-BUNDLE-03'; Category = 'Monitoring' },
    @{ Name = 'Load Balancer Lite'; Price = 119.00; SKU = 'SKU-LB-LITE-04'; Category = 'Networking' },
    @{ Name = 'Backup Vault'; Price = 79.99; SKU = 'SKU-BACKUP-05'; Category = 'Storage' },
    @{ Name = 'Secure Login Pack'; Price = 59.99; SKU = 'SKU-LOGIN-06'; Category = 'Security' },
    @{ Name = 'Scale Worker'; Price = 189.00; SKU = 'SKU-SCALE-07'; Category = 'Compute' },
    @{ Name = 'Event Router'; Price = 169.00; SKU = 'SKU-EVENT-08'; Category = 'Integration' },
    @{ Name = 'Metrics Lens'; Price = 99.00; SKU = 'SKU-METRICS-09'; Category = 'Observability' },
    @{ Name = 'Cloud Cost Guard'; Price = 129.00; SKU = 'SKU-COST-10'; Category = 'FinOps' },
    @{ Name = 'Storage Archive'; Price = 219.00; SKU = 'SKU-ARCHIVE-11'; Category = 'Storage' },
    @{ Name = 'AI Assist Desk'; Price = 179.00; SKU = 'SKU-AI-12'; Category = 'AI' },
    @{ Name = 'Dev Portal'; Price = 139.00; SKU = 'SKU-DEV-13'; Category = 'Developer Tools' },
    @{ Name = 'Secrets Vault'; Price = 159.00; SKU = 'SKU-SECRETS-14'; Category = 'Security' },
    @{ Name = 'Latency Monitor'; Price = 109.00; SKU = 'SKU-LATENCY-15'; Category = 'Monitoring' },
    @{ Name = 'Queue Bridge'; Price = 149.00; SKU = 'SKU-QUEUE-16'; Category = 'Integration' },
    @{ Name = 'Container Kit'; Price = 199.00; SKU = 'SKU-CONTAINER-17'; Category = 'Compute' },
    @{ Name = 'Network Shield'; Price = 89.00; SKU = 'SKU-SHIELD-18'; Category = 'Security' },
    @{ Name = 'Audit Trail'; Price = 69.00; SKU = 'SKU-AUDIT-19'; Category = 'Compliance' },
    @{ Name = 'SaaS Starter'; Price = 49.00; SKU = 'SKU-SAAS-20'; Category = 'Starter' },
    @{ Name = 'SaaS Growth'; Price = 99.00; SKU = 'SKU-SAAS-21'; Category = 'Starter' },
    @{ Name = 'SaaS Scale'; Price = 199.00; SKU = 'SKU-SAAS-22'; Category = 'Starter' },
    @{ Name = 'Region Router'; Price = 139.00; SKU = 'SKU-REGION-23'; Category = 'Networking' },
    @{ Name = 'Failover Kit'; Price = 179.00; SKU = 'SKU-FAILOVER-24'; Category = 'Reliability' },
    @{ Name = 'Upgrade Pack'; Price = 89.00; SKU = 'SKU-UPGRADE-25'; Category = 'Platform' },
    @{ Name = 'Workflow Engine'; Price = 229.00; SKU = 'SKU-WORKFLOW-26'; Category = 'Automation' },
    @{ Name = 'Config Watch'; Price = 59.00; SKU = 'SKU-CONFIG-27'; Category = 'Ops' },
    @{ Name = 'Insight Grid'; Price = 109.00; SKU = 'SKU-INSIGHT-28'; Category = 'Analytics' },
    @{ Name = 'Policy Center'; Price = 149.00; SKU = 'SKU-POLICY-29'; Category = 'Compliance' },
    @{ Name = 'Edge Cache'; Price = 89.00; SKU = 'SKU-CACHE-30'; Category = 'Performance' },
    @{ Name = 'Autoscale Pack'; Price = 199.00; SKU = 'SKU-AUTOSCALE-31'; Category = 'Compute' },
    @{ Name = 'KPI Board'; Price = 79.00; SKU = 'SKU-KPI-32'; Category = 'Analytics' },
    @{ Name = 'Access Control'; Price = 129.00; SKU = 'SKU-ACCESS-33'; Category = 'Security' },
    @{ Name = 'Sandbox Suite'; Price = 149.00; SKU = 'SKU-SANDBOX-34'; Category = 'Developer Tools' },
    @{ Name = 'Alert Router'; Price = 99.00; SKU = 'SKU-ALERT-35'; Category = 'Monitoring' },
    @{ Name = 'Stream Bridge'; Price = 179.00; SKU = 'SKU-STREAM-36'; Category = 'Integration' },
    @{ Name = 'Backup Mirror'; Price = 139.00; SKU = 'SKU-MIRROR-37'; Category = 'Storage' },
    @{ Name = 'Tenant Hub'; Price = 239.00; SKU = 'SKU-TENANT-38'; Category = 'Platform' },
    @{ Name = 'Cloud Router'; Price = 169.00; SKU = 'SKU-CLOUDROUTER-39'; Category = 'Networking' },
    @{ Name = 'Health Probe'; Price = 49.00; SKU = 'SKU-PROBE-40'; Category = 'Monitoring' },
    @{ Name = 'Service Mesh'; Price = 249.00; SKU = 'SKU-MESH-41'; Category = 'Platform' },
    @{ Name = 'Billing Lens'; Price = 109.00; SKU = 'SKU-BILLING-42'; Category = 'FinOps' },
    @{ Name = 'Container Guard'; Price = 179.00; SKU = 'SKU-GUARD-43'; Category = 'Security' },
    @{ Name = 'Ops Console'; Price = 129.00; SKU = 'SKU-CONSOLE-44'; Category = 'Operations' },
    @{ Name = 'Release Train'; Price = 149.00; SKU = 'SKU-RELEASE-45'; Category = 'Delivery' },
    @{ Name = 'Policy Watch'; Price = 89.00; SKU = 'SKU-POLICY-46'; Category = 'Compliance' },
    @{ Name = 'Redundancy Pack'; Price = 159.00; SKU = 'SKU-REDUNDANCY-47'; Category = 'Reliability' },
    @{ Name = 'Insight Stream'; Price = 119.00; SKU = 'SKU-STREAM-48'; Category = 'Analytics' },
    @{ Name = 'Cloud Runner'; Price = 199.00; SKU = 'SKU-RUNNER-49'; Category = 'Compute' },
    @{ Name = 'Portal Elite'; Price = 299.00; SKU = 'SKU-ELITE-50'; Category = 'Premium' }
)

function Get-ExcelColumnName {
    param([int]$Index)

    $columnName = ''
    $currentIndex = $Index
    while ($currentIndex -gt 0) {
        $remainder = ($currentIndex - 1) % 26
        $columnName = [char](65 + $remainder) + $columnName
        $currentIndex = [math]::Floor(($currentIndex - 1) / 26)
    }

    return $columnName
}

$encodedRows = for ($index = 0; $index -lt $products.Count; $index++) {
    $product = $products[$index]
    $rowNumber = $index + 2
    @"
    <row r="$rowNumber">
      <c r="A$rowNumber" t="inlineStr"><is><t>$($product.Name)</t></is></c>
      <c r="B$rowNumber"><v>$($product.Price)</v></c>
      <c r="C$rowNumber" t="inlineStr"><is><t>$($product.SKU)</t></is></c>
      <c r="D$rowNumber" t="inlineStr"><is><t>$($product.Category)</t></is></c>
    </row>
"@
}

$sheetXml = @"
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<worksheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">
  <sheetData>
    <row r="1">
      <c r="A1" t="inlineStr"><is><t>Name</t></is></c>
      <c r="B1" t="inlineStr"><is><t>Price</t></is></c>
      <c r="C1" t="inlineStr"><is><t>SKU</t></is></c>
      <c r="D1" t="inlineStr"><is><t>Category</t></is></c>
    </row>
$($encodedRows -join "`n")
  </sheetData>
</worksheet>
"@

$workbookXml = @"
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<workbook xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships">
  <sheets>
    <sheet name="Products" sheetId="1" r:id="rId1" />
  </sheets>
</workbook>
"@

$workbookRelsXml = @"
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
  <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet" Target="worksheets/sheet1.xml" />
</Relationships>
"@

$rootRelsXml = @"
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
  <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="xl/workbook.xml" />
</Relationships>
"@

$contentTypesXml = @"
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
  <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml" />
  <Default Extension="xml" ContentType="application/xml" />
  <Override PartName="/xl/workbook.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml" />
  <Override PartName="/xl/worksheets/sheet1.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml" />
  <Override PartName="/docProps/app.xml" ContentType="application/vnd.openxmlformats-officedocument.extended-properties+xml" />
  <Override PartName="/docProps/core.xml" ContentType="application/vnd.openxmlformats-package.core-properties+xml" />
</Types>
"@

$appXml = @"
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Properties xmlns="http://schemas.openxmlformats.org/officeDocument/2006/extended-properties">
  <Application>Microsoft Excel</Application>
</Properties>
"@

$coreXml = @"
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<cp:coreProperties xmlns:cp="http://schemas.openxmlformats.org/package/2006/metadata/core-properties" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:dcmitype="http://purl.org/dc/dcmitype/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <dc:title>SkyStore Products</dc:title>
  <dc:creator>GitHub Copilot</dc:creator>
  <cp:lastModifiedBy>GitHub Copilot</cp:lastModifiedBy>
  <dcterms:created xsi:type="dcterms:W3CDTF">2026-04-07T00:00:00Z</dcterms:created>
  <dcterms:modified xsi:type="dcterms:W3CDTF">2026-04-07T00:00:00Z</dcterms:modified>
</cp:coreProperties>
"@

$tempRoot = Join-Path $env:TEMP ('skystore-xlsx-' + [guid]::NewGuid().ToString('N'))
New-Item -ItemType Directory -Path $tempRoot -Force | Out-Null
New-Item -ItemType Directory -Path (Join-Path $tempRoot '_rels') -Force | Out-Null
New-Item -ItemType Directory -Path (Join-Path $tempRoot 'docProps') -Force | Out-Null
New-Item -ItemType Directory -Path (Join-Path $tempRoot 'xl') -Force | Out-Null
New-Item -ItemType Directory -Path (Join-Path $tempRoot 'xl\_rels') -Force | Out-Null
New-Item -ItemType Directory -Path (Join-Path $tempRoot 'xl\worksheets') -Force | Out-Null

[System.IO.File]::WriteAllText((Join-Path $tempRoot '[Content_Types].xml'), $contentTypesXml, [System.Text.Encoding]::UTF8)
[System.IO.File]::WriteAllText((Join-Path $tempRoot '_rels\.rels'), $rootRelsXml, [System.Text.Encoding]::UTF8)
[System.IO.File]::WriteAllText((Join-Path $tempRoot 'docProps\app.xml'), $appXml, [System.Text.Encoding]::UTF8)
[System.IO.File]::WriteAllText((Join-Path $tempRoot 'docProps\core.xml'), $coreXml, [System.Text.Encoding]::UTF8)
[System.IO.File]::WriteAllText((Join-Path $tempRoot 'xl\workbook.xml'), $workbookXml, [System.Text.Encoding]::UTF8)
[System.IO.File]::WriteAllText((Join-Path $tempRoot 'xl\_rels\workbook.xml.rels'), $workbookRelsXml, [System.Text.Encoding]::UTF8)
[System.IO.File]::WriteAllText((Join-Path $tempRoot 'xl\worksheets\sheet1.xml'), $sheetXml, [System.Text.Encoding]::UTF8)

$zipPath = [System.IO.Path]::ChangeExtension($OutputPath, '.zip')
if (Test-Path $zipPath) { Remove-Item $zipPath -Force }
if (Test-Path $OutputPath) { Remove-Item $OutputPath -Force }
Compress-Archive -Path (Join-Path $tempRoot '*') -DestinationPath $zipPath -Force
Move-Item -Path $zipPath -Destination $OutputPath
Remove-Item $tempRoot -Recurse -Force
Write-Host "Created $OutputPath"
