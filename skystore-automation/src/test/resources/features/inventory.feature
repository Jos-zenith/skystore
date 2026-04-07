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

  Scenario: User edits an inventory item
    Given the user is logged into the SkyStore Admin Portal
    When they enter product details for "Cloud Server Pro"
    And they edit that product to "Cloud Server Pro Deluxe"
    Then the item should reflect the updated name

  Scenario: User deletes an inventory item
    Given the user is logged into the SkyStore Admin Portal
    When they enter product details for "Cloud Server Pro"
    And they delete that product
    Then the item should be removed from the inventory list

  Scenario: User sees an error for invalid credentials
    Given the user is on the SkyStore login screen
    When they attempt to sign in with invalid credentials
    Then the login error should be visible

  Scenario: User opens the bulk upload view
    Given the user is logged into the SkyStore Admin Portal
    When they open the bulk upload view
    Then the bulk upload page should be visible
