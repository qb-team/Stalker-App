package qbteam.stalkerapp.homepage;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.qbteam.stalkerapp.contract.HomeContract;
import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;
import it.qbteam.stalkerapp.model.service.Storage;
import it.qbteam.stalkerapp.presenter.HomePresenter;

import static org.mockito.Mockito.verify;

public class HomePresenterTest {
/*
    private Organization o;
    private List<Organization> list;
    private String path="data/user/0/it.qbteam.stalkerapp/files/Organizzazioni.txt";
    private HomePresenter homePresenter;

    @Mock
    private HomeContract.View homeView;
    @Mock
    private HomeContract.Interactor storage;
    @Mock
    HomeContract.HomeListener homeListener;

    @Before
    public void setUpHomePresenter(){
        MockitoAnnotations.initMocks(this);
        list=new ArrayList<>();
        o= new Organization();
        o.setName("IMOLA");
        list.add(o);
        homePresenter = new HomePresenter(homeView);
    }

    @Test
    public void update() throws IOException, JSONException {
        homePresenter.updateFile(list,path);
        verify(storage).performUpdateFile(list,path);
    }*/
}
