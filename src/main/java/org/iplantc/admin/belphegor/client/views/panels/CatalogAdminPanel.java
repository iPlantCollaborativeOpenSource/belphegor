package org.iplantc.admin.belphegor.client.views.panels;

import java.util.ArrayList;

import org.iplantc.admin.belphegor.client.I18N;
import org.iplantc.admin.belphegor.client.services.AppTemplateAdminServiceFacade;
import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uiapplications.client.events.AnalysisCategorySelectedEvent;
import org.iplantc.core.uiapplications.client.events.AnalysisCategorySelectedEventHandler;
import org.iplantc.core.uiapplications.client.models.Analysis;
import org.iplantc.core.uiapplications.client.models.AnalysisGroup;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.BorderLayoutEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CatalogAdminPanel extends ContentPanel {
    private ArrayList<HandlerRegistration> handlers;
    private CatalogCategoryAdminPanel catPanel;
    private CatalogMainAdminPanel mainPanel;

    private BorderLayoutData dataWest;
    private BorderLayoutData dataCenter;

    public CatalogAdminPanel() {
        setCaption();
        initHandlers();
        initPanels();
        initLayout();

    }

    private void initHandlers() {
        EventBus eventbus = EventBus.getInstance();
        handlers = new ArrayList<HandlerRegistration>();
        handlers.add(eventbus.addHandler(AnalysisCategorySelectedEvent.TYPE,
                new AnalysisCategorySelectedEventHandlerImpl()));
    }

    private void setCaption() {
        setHeading(I18N.DISPLAY.adminApp());
    }

    private void initModel() {
        // TODO Auto-generated method stub

    }

    private BorderLayoutData initLayoutRegion(LayoutRegion region, float size, boolean collapsible) {
        BorderLayoutData ret = new BorderLayoutData(region);

        if (size > 0) {
            ret.setSize(size);
        }

        ret.setCollapsible(collapsible);
        ret.setSplit(true);

        return ret;
    }

    private void initLayout() {
        BorderLayout layout = new BorderLayout();

        // make sure we re-draw when a panel expands
        layout.addListener(Events.Expand, new Listener<BorderLayoutEvent>() {
            public void handleEvent(BorderLayoutEvent be) {
                layout();
            }
        });

        setLayout(layout);

        dataWest = initLayoutRegion(LayoutRegion.WEST, getWestWidth(), true);
        dataCenter = initLayoutRegion(LayoutRegion.CENTER, 0, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        compose();
    }

    private void initPanels() {
        catPanel = new CatalogCategoryAdminPanel();
        mainPanel = new CatalogMainAdminPanel();
    }

    private void compose() {
        add(catPanel, dataWest);
        add(mainPanel, dataCenter);
    }

    private int getWestWidth() {
        return 220;
    }

    private class AnalysisCategorySelectedEventHandlerImpl implements
            AnalysisCategorySelectedEventHandler {

        @Override
        public void onSelection(AnalysisCategorySelectedEvent event) {
            if (event.getSourcePanel() == catPanel) {
                mainPanel.setHeading(event.getGroup().getName());
                updateAnalysesListing(event.getGroup());
            }
        }

    }

    private void updateAnalysesListing(final AnalysisGroup group) {
        AppTemplateAdminServiceFacade facade = new AppTemplateAdminServiceFacade();
        mask(I18N.DISPLAY.loadingMask());
        facade.getAnalysis(group.getId(), new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ArrayList<Analysis> analyses = new ArrayList<Analysis>();

                JSONArray templates = JsonUtil.getArray(JsonUtil.getObject(result), "templates");
                if (templates != null) {
                    for (int i = 0; i < templates.size(); i++) {
                        Analysis analysis = new Analysis(JsonUtil.getObjectAt(templates, i));
                        analyses.add(analysis);
                    }
                }

                mainPanel.seed(analyses, group);
                unmask();
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                unmask();
            }
        });

    }
}