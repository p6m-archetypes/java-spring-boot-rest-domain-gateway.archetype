package {{ root_package }}.server;

import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import {{ root_package }}.core.{{ ProjectPrefix }}{{ ProjectSuffix }}Core;

@SpringBootTest(classes = { })
public class {{ ProjectPrefix }}{{ ProjectSuffix }}ServerTest {

    @Mock
    private {{ ProjectPrefix }}{{ ProjectSuffix }}Core {{ projectPrefix }}{{ ProjectSuffix }}Core;
}
