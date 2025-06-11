package br.ufes.dwws.cantosparamissa.core.domain;

public enum Role {
  ADMIN("admin");

  private final String value;

  Role(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}
