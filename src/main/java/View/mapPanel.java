package View;

import Model.CarData;
import View.RoutePainter;

import org.jfree.chart.ChartColor;
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

/**
 * A {@link JPanel} containing a map of the world.
 * The map of the world is provided by OpenStreetMap. Tracks are added to map to display the trip made by the user.
 */
public class mapPanel extends JPanel {

    /** The object containing the map of the world. **/
    private JXMapViewer map;
    /** The data being used to add tracks on the map **/
    private ArrayList<ArrayList<CarData>> data;
    /** A {@link List} of the added painters({@link Waypoint}, tracks) on the map **/
    private List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();

    /**
     * Build a mapPanel containing the map of the world and the tracks representing the trips made by the user
     * @param cd The data containing the geo positions of the trips.
     */
    public mapPanel(ArrayList<ArrayList<CarData>> cd){

        this.map = new JXMapViewer();
        this.data = cd;

        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        this.map.setTileFactory(tileFactory);


        // Create a compound painter that uses both the route-painter and the waypoint-painter
        this.painters = new ArrayList<Painter<JXMapViewer>>();

        List<GeoPosition> track =null;
        int num = 0;
        Paint[] cs =  ChartColor.createDefaultPaintArray();
        for(ArrayList<CarData> data : cd) {
            track = new ArrayList<>();
            for (CarData c : data) {
                track.add(new GeoPosition(c.getLatitude(), c.getLongitude()));
            }
            this.painters.add(new RoutePainter(track, (Color) cs[num%cs.length]));
            num++;
        }

        // Set the focus
        //this.map.zoomToBestFit(new HashSet<GeoPosition>(track), 0.01);
        this.map.setZoom(10);
        this.map.setCenterPosition(track.get(0));

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

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(this.painters);
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

    /**
     * Return the object containing the map
     * @return A {@link JXMapViewer} object, contains the map of the worlds and the track of the trips
     */
    public JXMapViewer getMap(){
        return this.map;
    }

    /**
     * Add {@link Waypoint} on the map at a precise point from the data.
     * Called on mouse hovering on the charts
     * @param x The index of the data being hovered by the user
     */
    public void addWaypoint(int x){
        ArrayList<CarData> carData = new ArrayList<>();
        for(ArrayList<CarData> cd : this.data){
            for(CarData c : cd){
                carData.add(c);
            }
        }
        try {
            CarData pickedData = carData.get(x);
            GeoPosition g = new GeoPosition(pickedData.getLatitude(), pickedData.getLongitude());

            Set<Waypoint> waypoints = new HashSet<Waypoint>();
            waypoints.add(new DefaultWaypoint(g));

            WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
            waypointPainter.setWaypoints(waypoints);
            CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(this.painters);
            painter.addPainter(waypointPainter);
            this.map.setOverlayPainter(painter);
            this.map.revalidate();
            this.map.repaint();
        }catch(Exception e){

        }
    }

}
