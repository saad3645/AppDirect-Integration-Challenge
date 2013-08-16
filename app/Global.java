import play.*;
import play.libs.*;
import com.avaje.ebean.Ebean;
import models.*;

import java.util.*;

public class Global extends GlobalSettings
{
    @SuppressWarnings("rawtypes")
    @Override
    public void onStart(Application app)
    {
        //if (User.find().findRowCount() == 0) {
        //    Ebean.save((List) Yaml.load("initial-data.yml"));
        //}

    }
}
