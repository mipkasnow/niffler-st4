package guru.qa.niffler.jupiter;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static guru.qa.niffler.jupiter.User.UserType.*;

public class UsersQueueExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE
      = ExtensionContext.Namespace.create(UsersQueueExtension.class);

  private static Map<User.UserType, Queue<UserJson>> users = new ConcurrentHashMap<>();

  static {
    Queue<UserJson> withFriendsQueue = new ConcurrentLinkedQueue<>();
    Queue<UserJson> invitationSendQueue = new ConcurrentLinkedQueue<>();
    Queue<UserJson> invitationReceivedQueue = new ConcurrentLinkedQueue<>();

    withFriendsQueue.add(user("dima", "12345", WITH_FRIENDS));
    withFriendsQueue.add(user("duck", "12345", WITH_FRIENDS));
    invitationSendQueue.add(user("bee", "12345", INVITATION_SEND));
    invitationReceivedQueue.add(user("barsik", "12345", INVITATION_RECEIVED));

    users.put(WITH_FRIENDS, withFriendsQueue);
    users.put(INVITATION_SEND, invitationSendQueue);
    users.put(INVITATION_RECEIVED, invitationReceivedQueue);
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    Parameter[] parameters = getParameters(context);
    Map<User.UserType, UserJson> testCandidates = new HashMap<>();

    for (Parameter parameter : parameters) {
      User annotation = parameter.getAnnotation(User.class);

      if (annotation != null && parameter.getType().isAssignableFrom(UserJson.class) && parameters.length > testCandidates.size()) {
        UserJson testCandidate = null;

        Queue<UserJson> queue = users.get(annotation.value());

        while (testCandidate == null) {
          testCandidate = queue.poll();
        }
        testCandidates.put(testCandidate.testData().userType(), testCandidate);
      }
    }

    context.getStore(NAMESPACE).put(context.getUniqueId(), testCandidates);
  }

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {
    Map<User.UserType, UserJson> usersFromTest = context.getStore(NAMESPACE)
            .get(context.getUniqueId(), Map.class);

    for (User.UserType userType : usersFromTest.keySet()) {
      users.get(userType).add(usersFromTest.get(userType));
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter()
        .getType()
        .isAssignableFrom(UserJson.class) &&
        parameterContext.getParameter().isAnnotationPresent(User.class);
  }

  @Override
  public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return (UserJson) extensionContext.getStore(NAMESPACE)
        .get(extensionContext.getUniqueId(), Map.class)
            .get(parameterContext.findAnnotation(User.class).get().value());
  }

  private static UserJson user(String username, String password, User.UserType userType) {
    return new UserJson(
        null,
        username,
        null,
        null,
        CurrencyValues.RUB,
        null,
        null,
        new TestData(
            password,
            userType
        )
    );
  }

  private static Parameter[] getParameters(ExtensionContext context) {
    Method[] methods = context.getRequiredTestClass().getDeclaredMethods();

    for (Method method: methods) {
      if(method.isAnnotationPresent(BeforeEach.class) && method.getParameters().length > 0) {
          return method.getParameters();
      }
    }

    return context.getRequiredTestMethod().getParameters();
  }
}
