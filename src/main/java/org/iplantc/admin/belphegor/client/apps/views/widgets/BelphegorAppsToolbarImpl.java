package org.iplantc.admin.belphegor.client.apps.views.widgets;

import org.iplantc.core.uiapps.client.models.autobeans.App;
import org.iplantc.core.uiapps.client.views.widgets.AppSearchField;
import org.iplantc.core.uiapps.client.views.widgets.proxy.AppLoadConfig;
import org.iplantc.core.uiapps.client.views.widgets.proxy.AppSearchAutoBeanFactory;
import org.iplantc.core.uiapps.client.views.widgets.proxy.AppSearchRpcProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

/**
 * @author jstroot
 * 
 */
public class BelphegorAppsToolbarImpl implements BelphegorAppsToolbar {

    private static BelphegorAppsViewToolbarUiBinder uiBinder = GWT.create(BelphegorAppsViewToolbarUiBinder.class);

    @UiTemplate("BelphegorAppsViewToolbar.ui.xml")
    interface BelphegorAppsViewToolbarUiBinder extends UiBinder<Widget, BelphegorAppsToolbarImpl> {
    }

    private final Widget widget;
    private Presenter presenter;
    private AppSearchRpcProxy proxy;

    @UiField
    ToolBar toolBar;

    @UiField
    TextButton addCategory;

    @UiField
    TextButton renameCategory;

    @UiField
    AppSearchField appSearch;

    @UiField
    TextButton delete;

    @UiField
    TextButton restoreApp;

    @UiField
    PagingLoader<FilterPagingLoadConfig, PagingLoadResult<App>> loader;

    @UiFactory
    PagingLoader<FilterPagingLoadConfig, PagingLoadResult<App>> createPagingLoader() {
        proxy = new AppSearchRpcProxy();
        PagingLoader<FilterPagingLoadConfig, PagingLoadResult<App>> loader = new PagingLoader<FilterPagingLoadConfig, PagingLoadResult<App>>(
                proxy);

        AppLoadConfig appLoadConfig = AppSearchAutoBeanFactory.instance.loadConfig().as();
        loader.useLoadConfig(appLoadConfig);

        return loader;
    }

    @UiFactory
    AppSearchField createAppSearchField() {
        return new AppSearchField(loader);
    }

    public BelphegorAppsToolbarImpl() {
        widget = uiBinder.createAndBindUi(this);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @UiHandler("addCategory")
    public void addCategoryClicked(SelectEvent event) {
        presenter.onAddAppGroupClicked();
    }

    @UiHandler("renameCategory")
    public void renameCategoryClicked(SelectEvent event) {
        presenter.onRenameAppGroupClicked();
    }

    @UiHandler("delete")
    public void deleteClicked(SelectEvent event) {
        presenter.onDeleteClicked();
    }

    @UiHandler("restoreApp")
    public void restoreAppClicked(SelectEvent event) {
        presenter.onRestoreAppClicked();
    }

    @Override
    public void setAddAppGroupButtonEnabled(boolean enabled) {
        addCategory.setEnabled(enabled);
    }

    @Override
    public void setRenameAppGroupButtonEnabled(boolean enabled) {
        renameCategory.setEnabled(enabled);
    }

    @Override
    public void setDeleteButtonEnabled(boolean enabled) {
        delete.setEnabled(enabled);
    }

    @Override
    public void setRestoreButtonEnabled(boolean enabled) {
        restoreApp.setEnabled(enabled);
    }

    @Override
    public AppSearchRpcProxy getAppSearchRpcProxy() {
        return proxy;
    }
}