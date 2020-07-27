package View;

import Model.CarData;
import View.RoutePainter;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.util.*;
import java.util.List;

public class mapPanel extends JPanel {

    private JXMapViewer map;

    public mapPanel(ArrayList<ArrayList<CarData>> cd){

        this.map = new JXMapViewer();

        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        this.map.setTileFactory(tileFactory);


        // Create a compound painter that uses both the route-painter and the waypoint-painter
        List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();

        List<GeoPosition> track =null;
        for(ArrayList<CarData> data : cd) {
            track = new ArrayList<>();
            for (CarData c : data) {
                track.add(new GeoPosition(c.getLatitude(), c.getLongitude()));
            }
            Random rand = new Random();
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            Color random = new Color(r,g,b);
            painters.add(new RoutePainter(track, random));
        }

        // Set the focus
        this.map.zoomToBestFit(new HashSet<GeoPosition>(track), 1);
        //this.map.setZoom(12);

       /* // Create waypoints from the geo-positions
        Set<Waypoint> waypoints = new HashSet<Waypoint>();

        for(GeoPosition g : track){
            waypoints.add(new DefaultWaypoint(g));
        }
*/

        // Add interactions
        MouseInputListener mia = new PanMouseInputListener( this.map);
        this.map.addMouseListener(mia);
        this.map.addMouseMotionListener(mia);

        this.map.addMouseListener(new CenterMapListener( this.map));

        this.map.addMouseWheelListener(new ZoomMouseWheelListenerCursor( this.map));

        this.map.addKeyListener(new PanKeyListener( this.map));



       /* // Create a waypoint painter that takes all the waypoints
        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
        waypointPainter.setWaypoints(waypoints);
*/

       // painters.add(waypointPainter);

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        this.map.setOverlayPainter(painter);


       /* mapViewer.addPropertyChangeListener("zoom", new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                updateWindowTitle(frame, mapViewer);
            }
        });

        mapViewer.addPropertyChangeListener("center", new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                updateWindowTitle(frame, mapViewer);
            }
        });*/

    }

    public JXMapViewer getMap(){
        return this.map;
    }


}
