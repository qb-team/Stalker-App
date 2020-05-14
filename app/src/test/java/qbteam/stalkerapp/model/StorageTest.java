package qbteam.stalkerapp.model;

import android.os.Environment;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.qbteam.stalkerapp.contract.HomeContract;
import it.qbteam.stalkerapp.contract.MyStalkersListContract;
import it.qbteam.stalkerapp.model.backend.dataBackend.Organization;
import it.qbteam.stalkerapp.model.backend.dataBackend.OrganizationMovement;
import it.qbteam.stalkerapp.model.service.Storage;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StorageTest {
    private Organization organization;
    private static  String path = "filePath";
    private Storage storage;
    private List<Organization> list;
    private OrganizationMovement organizationMovement;
    @Mock
    HomeContract.HomeListener homeListener;
    private MyStalkersListContract.MyStalkerListener myStalkerListener;
    @Before
    public void setUpStorageTest(){
        organization=new Organization();
        organization.setId(11L);
        organization.setName("Imola Informatica");
        list= new ArrayList<>();
        list.add(organization);
        organizationMovement = new OrganizationMovement();
        myStalkerListener=mock(MyStalkersListContract.MyStalkerListener.class);
        storage=new Storage(homeListener,myStalkerListener);
    }
    @Test
    public void removeOrganization() throws IOException, JSONException {

        storage.performRemoveLocal(organization,list,path);
    }

    @Test
    public void addorganization() throws IOException, JSONException {

        storage.performAddOrganizationLocal(organization,list,path);
    }

    @Test
    public void checkFile(){

        storage.performCheckFileLocal(path);
    }

    @Test
    public void updateFile() throws IOException, JSONException {

        storage.performUpdateFile(list,path);
    }

    @Test
    public void serializeMovement() throws IOException {
        Storage.serializeMovementInLocal(organizationMovement);
    }

    @Test
    public void deserializeMovement() throws IOException, ClassNotFoundException {
        System.out.println(Environment.getExternalStorageState());
        Storage.deserializeMovementInLocal();
    }



}
