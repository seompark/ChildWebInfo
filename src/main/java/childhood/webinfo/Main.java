package main.java.childhood.webinfo;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Collection;

public class Main extends PluginBase {

    public void onEnable() {
        try {
            HttpServer server = HttpServer.create( new InetSocketAddress( 80 ), 0 );
            server.createContext( "/", new RootHandler() );
            server.setExecutor( null );
            server.start();
        } catch ( IOException e ) {
            this.getLogger().info( TextFormat.RED + "[CWI] Failed to create HttpServer" );
        }
    }

    static class RootHandler implements HttpHandler {
        public void handle( HttpExchange httpExchange ) throws IOException {
            Server server = Server.getInstance();
            Collection<Player> playerCollection = server.getOnlinePlayers().values();
            String playerCount = Integer.toString( playerCollection.size() );
            String maxPlayer = Integer.toString( server.getMaxPlayers() );
            String playerNames = "";
            for ( Player player : playerCollection ) {
                playerNames += " " + player.getName();
            }
            String response = "ChildWebInfo\n\nPlayers: " + playerCount + "/" + maxPlayer + " (" + playerNames + " )";
            httpExchange.sendResponseHeaders( 200, response.length() );
            OutputStream os = httpExchange.getResponseBody();
            os.write( response.getBytes() );
            os.close();
        }
    }

}