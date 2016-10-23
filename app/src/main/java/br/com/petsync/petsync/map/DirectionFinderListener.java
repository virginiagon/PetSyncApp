package br.com.petsync.petsync.map;

import java.util.List;

public interface DirectionFinderListener {

    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> routes);

}
