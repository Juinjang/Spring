package umc.th.juinjang.api.limjang.controller.parameter;

public enum LimjangSortOptions {
  UPDATED("UPDATED"),
  CREATED("CREATED"),
  STAR("STAR");

  private final String value;

  LimjangSortOptions(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
