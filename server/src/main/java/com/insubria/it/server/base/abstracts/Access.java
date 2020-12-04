package com.insubria.it.server.base.abstracts;


/**
 * The abstract class Access defines the signatures of the methods that will be defined and available only in the AccessController class (because of the protected visibility).
 * Used an abstract class instead of an interface because we want to make these methods protected and not public
 */
public abstract class Access {
  /**
   * The signature of the createDatabase method.
   * This method is defined in the AccessController class
   */
  protected abstract void createDatabase();

  /**
   * The signature of the askForCredentials method.
   * This method is defined in the AccessController class
   */
  protected abstract String[] askForCredentials();

  /**
   * The signature of the askAdministratorCredentials method.
   * This method is defined in the AccessController class
   */
  protected abstract String[] askAdministratorCredentials();

  /**
   * The signature of the checkAdminProfile method.
   * This method is defined in the AccessController class
   */
  protected abstract boolean checkAdminProfile();

  /**
   * The signature of the createAdminProfile method.
   * This method is defined in the AccessController class
   */
  protected abstract void createAdminProfile();
}
