import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rahulchowdhury on 12/2/16.
 * 
 * Model class for a Call Group for specifying API
 * calls in a group for Pacman to work
 * 
 * Made with love by Rahul Chowdhury
 */

public class CallGroup implements Parcelable {

    private long groupId;
    private int calls;

    public CallGroup() {
    }

    public CallGroup(long groupId, int calls) {
        this.groupId = groupId;
        this.calls = calls;
    }

    protected CallGroup(Parcel in) {
        this.groupId = in.readLong();
        this.calls = in.readInt();
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public int getCalls() {
        return calls;
    }

    public void setCalls(int calls) {
        this.calls = calls;
    }

    @Override
    public String toString() {
        return "CallGroup{" +
                "groupId=" + groupId +
                ", calls=" + calls +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        CallGroup callGroup = (CallGroup) o;
        return callGroup.getGroupId() == this.getGroupId();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.groupId);
        dest.writeInt(this.calls);
    }

    public static final Parcelable.Creator<CallGroup> CREATOR = new Parcelable.Creator<CallGroup>() {
        @Override
        public CallGroup createFromParcel(Parcel source) {
            return new CallGroup(source);
        }

        @Override
        public CallGroup[] newArray(int size) {
            return new CallGroup[size];
        }
    };
}
