package guru.qa.niffler.page_component;

import com.codeborne.selenide.SelenideElement;

public abstract class BaseComponent<T extends BaseComponent> {

  protected final SelenideElement self;

  public BaseComponent(SelenideElement self) {
    this.self = self;
  }

  public SelenideElement getSelf() {
    return self;
  }
}
