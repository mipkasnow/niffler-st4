package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.ex.TextsMismatch;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.model.SpendJson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.*;

public class SpendCollectionCondition {

  private static final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy", Locale.ENGLISH);
  private static final List<String> expectedTexts = new ArrayList<>();
  private static final List<String> actualTexts = new ArrayList<>();
  private static final HashMap<String, List<String>> mapResult = new HashMap<>();

  public static CollectionCondition spends(SpendJson... expectedSpends) {
    return new CollectionCondition() {

      @Override
      public void fail(CollectionSource collection, CheckResult lastCheckResult, @Nullable Exception cause, long timeoutMs) {
        throw new TextsMismatch(
                "Тексты в Spendings отличаются",
                collection,
                ((HashMap<String, List<String>>) lastCheckResult.getActualValue()).get("expected"),
                ((HashMap<String, List<String>>) lastCheckResult.getActualValue()).get("actual"),
                explanation,
                timeoutMs,
                cause
        );
      }

      @Nonnull
      @Override
      public CheckResult check(Driver driver, List<WebElement> elements) {
        if (elements.size() != expectedSpends.length) {
          return CheckResult.rejected("Incorrect table size", elements);
        }

        boolean checkPassed = false;

        for (int i = 0; i < elements.size(); i++) {

          List<WebElement> tdsRowSpend = elements.get(i).findElements(By.cssSelector("td"));
          SpendJson expectedSpend = expectedSpends[i];
          checkPassed = compareRowSpendAndExpected(tdsRowSpend, expectedSpend);

          if (!checkPassed) {
            break;
          }
        }

        if (checkPassed) {
          return CheckResult.accepted();
        } else {
          expectedTexts.addAll(Arrays.stream(expectedSpends).map(Record::toString).toList());
          mapResult.put("actual", actualTexts);
          mapResult.put("expected", expectedTexts);
          return CheckResult.rejected("Incorrect spends content", mapResult);
        }
      }

      @Override
      public boolean missingElementSatisfiesCondition() {
        return false;
      }
    };
  }

  private static boolean compareRowSpendAndExpected(List<WebElement> tdsRowSpend, SpendJson expectedSpend) {
    String dateA = tdsRowSpend.get(1).getText();
    String dateE = formatter.format(expectedSpend.spendDate());
    String amountA = tdsRowSpend.get(2).getText();
    String amountE = expectedSpend.amount().toString();
    String currencyA = tdsRowSpend.get(3).getText();
    String currencyE = expectedSpend.currency().toString();
    String categoryA = tdsRowSpend.get(4).getText();
    String categoryE = expectedSpend.category();
    String descriptionA = tdsRowSpend.get(5).getText();
    String descriptionE = expectedSpend.description();

    actualTexts.addAll(tdsRowSpend.stream().map(WebElement::getText).toList());

    return dateA.equals(dateE)
            && amountA.equals(amountE)
            && currencyA.equals(currencyE)
            && categoryA.equals(categoryE)
            && descriptionA.equals(descriptionE);
  }


}
