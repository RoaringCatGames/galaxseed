package com.roaringcatgames.galaxseed;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSErrorException;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.pods.google.GGLContext;
import org.robovm.pods.google.GGLContextMobileAds;
import org.robovm.pods.google.mobileads.GADBannerView;
import org.robovm.pods.google.mobileads.GADRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IOSLauncher extends IOSApplication.Delegate implements IAdController {

    GADBannerView gadBannerView;

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();

        try {
            GGLContext.getSharedInstance().configure();
        } catch (NSErrorException e) {
            System.err.println("Error configuring the Google context: " + e.getError());
        }



        return new IOSApplication(App.Initialize(this, null), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

    @Override
    public void showBannerAd(AdPlacement placement) {
        if(gadBannerView == null) {
            GADRequest request = new GADRequest();
            // Display test ads on the simulator.
            List<String> keywords = new ArrayList<>();
            keywords.add("Arcade");
            keywords.add("Gardening");
            keywords.add("Plants");
            keywords.add("Space");

            request.setTestDevices(Arrays.asList(GADRequest.getSimulatorID()));
            request.setKeywords(keywords);

            String bannerId = GGLContextMobileAds.getSharedInstance().getConfiguration().getBannerAdUnitID();
            gadBannerView = new GADBannerView();
            gadBannerView.setAdUnitID(bannerId);

            //Set and Load BannerAd
            UIViewController viewController = UIApplication.getSharedApplication().getKeyWindow().getRootViewController();
            gadBannerView.setRootViewController(viewController);
            viewController.getView().addSubview(gadBannerView);
            gadBannerView.loadRequest(request);
        } else {
            gadBannerView.setHidden(false);
        }
    }

    @Override
    public void hideBannerAd(AdPlacement placement) {
        if(gadBannerView != null){
            gadBannerView.setHidden(true);
        }
    }
}