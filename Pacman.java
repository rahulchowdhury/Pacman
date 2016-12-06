import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahulchowdhury on 12/2/16.
 * <p>
 * Pacman - Parallel API Call Manager
 * <p>
 * This utility class allows you to manage API calls that happen
 * in parallel and receive a combined or reduced callback when
 * all the specified calls have been performed
 * <p>
 * Just setup Pacman with the required API calls and update as
 * and when the calls are made
 * <p>
 * Made with love by Rahul Chowdhury
 */

public class Pacman {

    private static final List<CallGroup> mRequestedCallGroups = new ArrayList<>();
    private static final List<CallGroup> mCompletedCallGroups = new ArrayList<>();
    private static OnCallsCompleteListener mOnCallsCompleteListener;

    /**
     * Check whether all specified API calls are completed
     */
    private static void checkForApiCallsCompletion() {
        boolean allCallsComplete = true;

        for (CallGroup callGroup : mRequestedCallGroups) {
            if (!mCompletedCallGroups.contains(callGroup)) {
                allCallsComplete = false;
                break;
            } else {
                int indexOfSelectedCallGroup = mCompletedCallGroups.indexOf(callGroup);
                CallGroup selectedGroup = mCompletedCallGroups.get(indexOfSelectedCallGroup);

                if (selectedGroup.getCalls() < callGroup.getCalls()) {
                    allCallsComplete = false;
                    break;
                }
            }
        }

        //If all calls are made then fire a callback to the listener
        if (allCallsComplete) {
            mOnCallsCompleteListener.onCallsCompleted();
        }
    }

    /**
     * Required to initialize Pacman with the API call groups details
     * and a callback listener to be notified when all calls have been
     * completed
     *
     * @param callGroups              An ArrayList of CallGroup objects with details for the call groups
     * @param onCallsCompleteListener A callback listener to get notified when all calls are finished
     */
    public static void initialize(@NonNull List<CallGroup> callGroups,
                                  @NonNull OnCallsCompleteListener onCallsCompleteListener) {
        mRequestedCallGroups.clear();
        mCompletedCallGroups.clear();
        mRequestedCallGroups.addAll(callGroups);

        mOnCallsCompleteListener = onCallsCompleteListener;
    }

    /**
     * Post an API call update to a specific call group
     *
     * @param groupId ID for the Call Group
     */
    public static void postCallGroupUpdate(long groupId) {
        CallGroup callGroupToUpdate = new CallGroup(groupId, 0);
        if (!mRequestedCallGroups.contains(callGroupToUpdate)) {
            return;
        }

        if (mCompletedCallGroups.contains(callGroupToUpdate)) {
            int indexOfSpecifiedCallGroup = mCompletedCallGroups.indexOf(callGroupToUpdate);
            CallGroup specifiedCallGroup = mCompletedCallGroups.get(indexOfSpecifiedCallGroup);

            int callsMade = specifiedCallGroup.getCalls() + 1;
            specifiedCallGroup.setCalls(callsMade);

            mCompletedCallGroups.remove(indexOfSpecifiedCallGroup);
            mCompletedCallGroups.add(specifiedCallGroup);
        } else {
            mCompletedCallGroups.add(new CallGroup(groupId, 1));
        }

        checkForApiCallsCompletion();
    }

    /**
     * Post an API call update to a specific call group and also
     * increase the no of calls to expect for that group
     *
     * @param groupId    ID for the specific call group
     * @param callsToAdd No of calls to add to that group
     */
    public static void postCallGroupUpdate(long groupId, int callsToAdd) {
        CallGroup callGroupToUpdate = new CallGroup(groupId, 0);
        if (mRequestedCallGroups.contains(callGroupToUpdate)) {
            int indexOfSpecifiedCallGroup = mRequestedCallGroups.indexOf(callGroupToUpdate);
            CallGroup specifiedCallGroup = mRequestedCallGroups.get(indexOfSpecifiedCallGroup);

            int callsToMake = specifiedCallGroup.getCalls() + callsToAdd;
            specifiedCallGroup.setCalls(callsToMake);

            mRequestedCallGroups.remove(indexOfSpecifiedCallGroup);
            mRequestedCallGroups.add(specifiedCallGroup);
        }

        postCallGroupUpdate(groupId);
    }

    public interface OnCallsCompleteListener {
        void onCallsCompleted();
    }
}
