Feature: Inventory management
  As a SkyStore admin
  I want to add premium catalog items
  So that the cloud inventory stays current

  Scenario: User adds a premium item to the cloud inventory
    Given the user is logged into the SkyStore Admin Portal
    When they enter product details for "Cloud Server Pro"
    Then the item should be visible in the inventory list

  Scenario: User switches cloud region and verifies breadcrumbs
    Given the user is logged into the SkyStore Admin Portal
    When they switch cloud region to "eu-west-1"
    And they open cloud settings from the sidebar
    Then the breadcrumb should contain "Cloud Configuration"
    And the selected region should be "eu-west-1"
