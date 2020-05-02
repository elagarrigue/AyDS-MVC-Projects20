package ayds.nene.movieinfo.moredetails.fulllogic;

import ayds.nene.movieinfo.home.model.entities.OmdbMovie;
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
            .baseUrl("https://en.wikipedia.org/w/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();

    WikipediaAPI wikiAPI = retrofit.create(WikipediaAPI.class);

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

            callResponse = wikiAPI.getTerm(movie.getTitle() + " " + movie.getYear() + " film").execute();


            System.out.println("JSON2 " + callResponse.body());



            Gson gson = new Gson();
            JsonObject jobj = gson.fromJson(callResponse.body(), JsonObject.class);
            JsonObject query = jobj.get("query").getAsJsonObject();

            Iterator<JsonElement> resultIterator = query.get("search").getAsJsonArray().iterator();

            JsonObject result = null;
            JsonElement extract = null;
            String pageId = null;

            if (resultIterator.hasNext()) {
              result = resultIterator.next().getAsJsonObject();

              extract = result.get("snippet");
              pageId = result.get("pageid").getAsString();

            }






            if (extract == null) {
              text = "No Results";
            } else {
              text = extract.getAsString().replace("\\n", "\n") + "...";

              text = textToHtml(text, movie.getTitle());

              text+="\n" + "<a href=https://en.wikipedia.org/?curid=" + pageId +">View Article</a>";

              path = "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png";


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

    JFrame frame = new JFrame("Movie Info Ne-ne");
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
