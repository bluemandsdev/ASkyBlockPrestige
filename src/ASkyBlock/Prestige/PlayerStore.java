package ASkyBlock.Prestige;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;

public class PlayerStore
{

    private File storageFile;
    private JSONObject values;

    public PlayerStore(File file)
    {
        this.storageFile = file;

        if (!this.storageFile.exists())
        {
            try
            {
                this.storageFile.createNewFile();

                try (BufferedWriter out = new BufferedWriter(new FileWriter(storageFile, true)))
                {
                    out.write("{}");
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public int getPlayerPrestige(String playerID)
    {
        return Integer.parseInt(values.get(playerID).toString());
    }

    public void load()
    {
        JSONParser parser = new JSONParser();

        try
        {
            Object obj = parser.parse(new FileReader(this.storageFile.getAbsolutePath()));
            values = (JSONObject) obj;

        } catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }
    }

    public void save()
    {
        try (FileWriter file = new FileWriter(this.storageFile))
        {
            file.write(values.toJSONString());
            file.flush();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public boolean contains(String value)
    {
        return values.containsKey(value);
    }

    public void add(String key, int presitge)
    {
        values.put(key, presitge);
    }

    public void remove(String key)
    {
        values.remove(key);
    }

}
