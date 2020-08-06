package com.example.abcfivrr.utilities;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abcfivrr.model.TrackingDetail;

import java.util.ArrayList;

public class UserTracker {

    private RecyclerView recyclerView;

    public UserTracker(RecyclerView recyclerView)
    {
        this.recyclerView = recyclerView;
    }

    private long startTime = 0;

    private long endTime = 0;

    // Flag is required because 'addOnGlobalLayoutListener'
    // is called multiple times.
    // The flag limits the action inside 'onGlobalLayout' to only once.
    private boolean firstTrackFlag = false;
    private ArrayList<Integer> viewsViewed = new ArrayList<>();
    private ArrayList<TrackingDetail> trackingData = new ArrayList<>();
    private double minimumVisibleHeightThreshold = 30;

    public  ArrayList<TrackingDetail> returnDetail()
    {
        return trackingData;
    }
    public void startTracking() {
        recyclerView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver
                        .OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        if(!firstTrackFlag) {

                            startTime = System.currentTimeMillis();

                            int firstVisibleItemPosition = ((LinearLayoutManager)
                                    recyclerView.getLayoutManager())
                                    .findFirstVisibleItemPosition();

                            int lastVisibleItemPosition = ((LinearLayoutManager)
                                    recyclerView.getLayoutManager())
                                    .findLastVisibleItemPosition();

                            analyzeAndAddViewData(firstVisibleItemPosition,
                                    lastVisibleItemPosition);

                            firstTrackFlag = true;
                        }
                    }
                });
// Track the views when user scrolls through the recyclerview.
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    endTime = System.currentTimeMillis();

                    for (int trackedViewsCount = 0;
                         trackedViewsCount < viewsViewed.size();
                         trackedViewsCount++ ) {

                        trackingData.add(prepareTrackingData(String
                                        .valueOf(viewsViewed
                                                .get(trackedViewsCount)),
                                (endTime - startTime)/1000));
                    }


                    viewsViewed.clear();
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    startTime = System.currentTimeMillis();

                    int firstVisibleItemPosition = ((LinearLayoutManager)
                            recyclerView.getLayoutManager())
                            .findFirstVisibleItemPosition();

                    int lastVisibleItemPosition = ((LinearLayoutManager)
                            recyclerView.getLayoutManager())
                            .findLastVisibleItemPosition();

                    analyzeAndAddViewData(firstVisibleItemPosition,
                            lastVisibleItemPosition);
                }
            }
        });
    }

    public void stopTracking() {

        endTime = System.currentTimeMillis();

        int firstVisibleItemPosition = ((LinearLayoutManager)
                recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

        int lastVisibleItemPosition = ((LinearLayoutManager)
                recyclerView.getLayoutManager()).findLastVisibleItemPosition();

        analyzeAndAddViewData(firstVisibleItemPosition,
                lastVisibleItemPosition);

        for (int trackedViewsCount = 0; trackedViewsCount < viewsViewed.size();
             trackedViewsCount++ ) {

            trackingData.add(prepareTrackingData(String.valueOf(viewsViewed
                            .get(trackedViewsCount)),
                    (endTime - startTime)/1000));

            viewsViewed.clear();
        }
    }

    private void analyzeAndAddViewData(int firstVisibleItemPosition,
                                       int lastVisibleItemPosition) {

        // Analyze all the views
        for (int viewPosition = firstVisibleItemPosition;
             viewPosition <= lastVisibleItemPosition; viewPosition++) {

            Log.i("View being considered", String.valueOf(viewPosition));

            // Get the view from its position.
            View itemView = recyclerView.getLayoutManager()
                    .findViewByPosition(viewPosition);

            if (
                    getVisibleHeightPercentage(itemView) >=
                            minimumVisibleHeightThreshold) {
                viewsViewed.add(viewPosition);
            }
        }
    }

    private double getVisibleHeightPercentage(View view) {

        Rect itemRect = new Rect();
        view.getLocalVisibleRect(itemRect);

        // Find the height of the item.
        double visibleHeight = itemRect.height();
        double height = view.getMeasuredHeight();

        Log.i("Visible Height", String.valueOf(visibleHeight));
        Log.i("Measured Height", String.valueOf(height));

        double viewVisibleHeightPercentage = ((visibleHeight/height) * 100);
        Log.i("Percentage visible", String.valueOf(viewVisibleHeightPercentage));
        return viewVisibleHeightPercentage;
    }

    private TrackingDetail prepareTrackingData(String viewId, long viewDuration) {

        TrackingDetail trackingData = new TrackingDetail();

        trackingData.setViewId(viewId);
        trackingData.setViewDuration(viewDuration);

        return trackingData;
    }

}
