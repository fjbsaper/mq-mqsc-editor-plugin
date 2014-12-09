/*
 * SupportPac MS0S
 * (c) Copyright IBM Corp. 2007. All rights reserved.
 * 
 * Created on Apr 20, 2007
 *
 */
package com.ibm.mq.explorer.ms0s.mqscscripts.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import com.ibm.mq.explorer.ms0s.mqscscripts.tree.MQSCScriptsFileNodeFactory;
import com.ibm.mq.explorer.ms0s.mqscscripts.tree.MQSCScriptsTreeNodeProjectFolder;
import com.ibm.mq.explorer.ms0s.mqscscripts.tree.MQSCScriptsTreeNodeRootFolder;
import com.ibm.mq.explorer.ui.Common;

/**
 * @author jlowrey
 * 
 */
public class NewProjectAction implements IActionDelegate {

    IStructuredSelection mySel;
    private static CommonNavigator MQView;

    private CommonNavigator getActiveNavigator() {
        CommonNavigator nav = null;
        IViewReference view = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getActivePage()
                .findViewReference(Common.VIEWID_MQ_NAVIGATOR_VIEW);
        if (view != null) {
            IViewPart part = view.getView(false);
            if ((part != null) && (part instanceof CommonNavigator)) {
                IMenuManager menus = part.getViewSite().getActionBars().getMenuManager();
                menus.add(new GroupMarker(IWorkbenchActionConstants.FIND_EXT));
                nav = (CommonNavigator) part;
            }
        }
        return nav;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    @SuppressWarnings("restriction")
    public void run(IAction action) {
        // Check to see if my default project exists.
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace()
                .getRoot();

        workbench.getNewWizardRegistry().findWizard(
                BasicNewProjectResourceWizard.WIZARD_ID);
        // IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        // IWorkbenchWizard wizard = new NewScriptFileWizard();
        // This worked and is a viable option. Might want to extend it to allow
        // for adding folders.
        IWizardDescriptor npWiz = workbench.getNewWizardRegistry().findWizard(
                BasicNewProjectResourceWizard.WIZARD_ID);
        BasicNewProjectResourceWizard wizard = null;
        MQView = getActiveNavigator();
        
        try {
            wizard = (BasicNewProjectResourceWizard) npWiz.createWizard();
            StructuredSelection newSel = new StructuredSelection(
                    (Object) myWorkspaceRoot);
            wizard.init(workbench, newSel);
            WizardDialog dialog = new WizardDialog(
                    MQView.getViewSite().getShell()
                    //PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell() 
                    , wizard);
            dialog.open();
        } catch (CoreException e) {
            e.printStackTrace();
        }
        // new BasicNewProjectResourceWizard();
        // construct a selection that points to the IProject that's been
        // selected.
        // this worked great the first time. wheeeeeeee!
        // Object testObj = mySel.getFirstElement();
        // if ((null != testObj) && (testObj instanceof
        // MQSCScriptsTreeNodeRootFolder)) {
        // //Path location = new
        // Path(((MQSCScriptsTreeNodeProjectFolder)testObj).getPath());
        // myProj = myWorkspaceRoot.getContainerForLocation(location);
        // //Project(().toString());
        // }
        // FolderTreeContentPage.java:227
        // null pointer exception here. Presumably mySel.
        if (null == mySel) {
            return;
        }
        Object obj = mySel.getFirstElement();
        IProject newFolder = wizard.getNewProject();
        MQSCScriptsFileNodeFactory.addFolderNode(newFolder);
        if (obj != null) {
            if (obj instanceof MQSCScriptsTreeNodeRootFolder) {
                MQSCScriptsTreeNodeRootFolder myNode = (MQSCScriptsTreeNodeRootFolder) obj;
                myNode.refresh();
            }
            // this says I need to write an interface.
            if (obj instanceof MQSCScriptsTreeNodeProjectFolder) {
                MQSCScriptsTreeNodeProjectFolder myNode = (MQSCScriptsTreeNodeProjectFolder) obj;
                myNode.refresh();
            }

        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
     * .IAction, org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {
        if (selection != null && selection instanceof IStructuredSelection) {
            IStructuredSelection structuredSelection = (IStructuredSelection) selection;
            // Get the selected object
            Object obj = structuredSelection.getFirstElement();
            if (obj != null) {
                if (obj instanceof MQSCScriptsTreeNodeRootFolder
                        || obj instanceof MQSCScriptsTreeNodeProjectFolder) {
                    mySel = structuredSelection;
                }
            }
        }
    }

}
