package server.base.interfaces;


public interface Access {
  private String[] askForCredentials ();

  private String[] askAdministratorCredentials();

  private boolean checkAdminProfile ();

  private void createAdminProfile ();
