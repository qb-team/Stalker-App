package qbteam.stalkerapp.loginform;



import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;

import org.mockito.runners.MockitoJUnitRunner;
import it.qbteam.stalkerapp.ui.view.LoginFragment;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;


@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {

  /*  private FirebaseAuth mockAuth;
    private LoginContract.View loginView;
    private LoginModel loginModel;
    private LoginPresenter loginPresenter;
    private LoginContract.onLoginListener onLoginListener;
    public static final FirebaseApp MOCK_APP;
    private static final String DEFAULT_APP_NAME = "[DEFAULT]";
    private static Context CONTEXT = ApplicationProvider.getApplicationContext();


    static {
        FirebaseApp app = mock(FirebaseApp.class);
        when(app.get(eq(FirebaseAuth.class))).thenReturn(mock(FirebaseAuth.class));
        when(app.getApplicationContext()).thenReturn(CONTEXT);
        when(app.getName()).thenReturn(DEFAULT_APP_NAME);
        MOCK_APP = app;
    }
    @Before
    public void setUpLogin(){
        MockitoAnnotations.initMocks(this);
        loginPresenter=mock(LoginPresenter.class);
        loginView = mock(LoginContract.View.class);
        onLoginListener= mock(LoginContract.onLoginListener.class);
        mockAuth = mock(FirebaseAuth.getInstance().getClass());
        loginModel= new LoginModel(onLoginListener);
    }
    @Test
    public void login(){
        String email="prova@prova.it";
        String password="12345678";
        Fragment fragment= new LoginFragment();
        Task taskMock= mock(Task.class);
        when(mockAuth.signInWithEmailAndPassword(email,password)).thenReturn(taskMock);
        loginModel.performFirebaseLogin(fragment,email,password);
    }
*/
  /*
  private static String email="prova@prova.it";
  private static String password="12345678";


  @InjectMocks
  private LoginFragment loginFragment;
  @Before
  public void setUpCredentials(){
    loginFragment= new LoginFragment();

  }
  @Test
    public void testCredentials(){

      loginFragment.setEmail(email);
      loginFragment.setPass(password);
      verify(loginFragment).checkLoginDetails();

     // assertEquals(loginFragment.getEmailEditText().getText().toString(),email);
     // assertEquals(loginFragment.getPasswordEditText().getText().toString(),password);






  }*/
}
