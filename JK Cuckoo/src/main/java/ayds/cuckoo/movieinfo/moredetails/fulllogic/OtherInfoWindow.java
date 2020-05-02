package ayds.cuckoo.movieinfo.moredetails.fulllogic;

import ayds.cuckoo.movieinfo.home.model.entities.OmdbMovie;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Iterator;

public class OtherInfoWindow {
  private JPanel contentPane;
  private JTextPane textPane2;
  private JPanel imagePanel;

  public void getMoviePlot(OmdbMovie movie) {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.nytimes.com/svc/movies/v2/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();

    NYTimesAPI wikiAPI = retrofit.create(NYTimesAPI.class);

    textPane2.setContentType("text/html");

    // this is needed to open a link in the browser
    textPane2.addHyperlinkListener(e -> {
      if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
        System.out.println(e.getURL());
        Desktop desktop = Desktop.getDesktop();
        try {
          desktop.browse(e.getURL().toURI());
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });


    new Thread(new Runnable() {
      @Override
      public void run() {

        String text = DataBase.getSummary(movie.getTitle());

        String path =  DataBase.getImageUrl(movie.getTitle());

        if (text != null && path != null) { // exists in db

          text = "[*]" + text;
        } else { // get from service
          Response<String> callResponse;
          try {
            System.out.println("TITLE " + movie.getTitle());

            callResponse = wikiAPI.getTerm(movie.getTitle()).execute();

            System.out.println("JSON1 " + callResponse.raw());
            System.out.println("JSON2 " + callResponse.body());



            Gson gson = new Gson();
            JsonObject jobj = gson.fromJson(callResponse.body(), JsonObject.class);

            Iterator<JsonElement> resultIterator = jobj.get("results").getAsJsonArray().iterator();

            JsonObject result = null;

            while (resultIterator.hasNext()) {
              result = resultIterator.next().getAsJsonObject();
              System.out.println("result " +result);
              String year = result.get("publication_date").getAsString().split("-")[0];

              if (year.equals(movie.getYear())) break;
            }


            JsonElement extract = result.get("summary_short");
            JsonElement linkUrl = result.get("link").getAsJsonObject().get("url");

            JsonElement multimediaJson = result.get("multimedia");

            JsonObject multimedia = null;
            System.out.println("multimediaJson " + multimediaJson);

            if (multimediaJson.isJsonObject()) {
              multimedia =  multimediaJson.getAsJsonObject();
            }



            if (extract == null) {
              text = "No Results";
            } else {
              text = extract.getAsString().replace("\\n", "\n");

              text = textToHtml(text, movie.getTitle());

              text+="\n" + "<a href=" + linkUrl.getAsString() +">View Article</a>";



              if(multimedia != null)
                path = multimedia.get("src").getAsString();

              if (path == null) {
                path = "https://www.shareicon.net/data/256x256/2016/06/25/618683_new_256x256.png";
              }

              // save to DB  <o/

              DataBase.saveMovieInfo(movie.getTitle(), text, path);
            }


          } catch (Exception e1) {
            e1.printStackTrace();
          }
        }

        textPane2.setText(text);


        // set image
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }

        try {
          System.out.println("Get Image from " + path);
          URL url = new URL(path);
          BufferedImage image = ImageIO.read(url);
          System.out.println("Load image into frame...");
          JLabel label = new JLabel(new ImageIcon(image));
          imagePanel.add(label);

          // Refresh panel
          contentPane.validate();
          contentPane.repaint();

        } catch (Exception exp) {
          exp.printStackTrace();
        }

      }
    }).start();
  }

  public static void open(OmdbMovie movie) {

    OtherInfoWindow win = new OtherInfoWindow();

    JFrame frame = new JFrame("Movie Info Cuckoo");
    frame.setContentPane(win.contentPane);
    frame.pack();
    frame.setVisible(true);

    DataBase.createNewDatabase();
    DataBase.saveMovieInfo("test", "sarasa", "");


    System.out.println(DataBase.getSummary("test"));
    System.out.println(DataBase.getSummary("nada"));


    win.getMoviePlot(movie);
  }

  public static String textToHtml(String text, String term) {

    StringBuilder builder = new StringBuilder();

    builder.append("<font face=\"arial\">");

    String textWithBold = text
            .replace("'", "`")
            .replaceAll("(?i)" + term, "<b>" + term.toUpperCase() + "</b>");

    builder.append(textWithBold);

    builder.append("</font>");

    return builder.toString();
  }

}
