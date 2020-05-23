package qbteam.stalkerapp.loginform;



import org.junit.runner.RunWith;

import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;


@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {
/*
    private FirebaseAuth mockAuth;
    private LoginContract.View loginView;
    private LoginModel loginModel;
    private LoginPresenter loginPresenter;
    private LoginContract.onLoginListener onLoginListener;
    public static final FirebaseApp MOCK_APP;
    private static final String DEFAULT_APP_NAME = "[DEFAULT]";
    private static Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();


    static {
        FirebaseApp app = mock(FirebaseApp.class);
        when(app.get(eq(FirebaseAuth.class))).thenReturn(mock(FirebaseAuth.class));
        when(app.getApplicationContext()).thenReturn(appContext);
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
}
