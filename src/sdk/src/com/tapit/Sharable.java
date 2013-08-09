package com.tapit;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;

import java.util.HashMap;
import java.util.Map;

/**
 * Convenience class used for passing live objects between intents
 * pulled from http://stackoverflow.com/questions/2736389/how-to-pass-object-from-one-activity-to-another-in-android#7683528
 */
public final class Sharable implements Parcelable {

    private Object m_object;

    public static final Parcelable.Creator<Sharable> CREATOR = new  Parcelable.Creator<Sharable>() {
        public Sharable createFromParcel(Parcel in) {
            return new Sharable (in);
        }

        @Override
        public Sharable[] newArray(int size) {
            return new Sharable[size];
        }
    };

    public Sharable(final Object obj) {
        m_object = obj;
    }

    public Sharable(Parcel in) {
        readFromParcel(in);
    }

    Object obj() {
        return m_object;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel out, int flags) {
        // crashes here on Glen's Droid Bionic.
        final long val = SystemClock.elapsedRealtime() + m_object.hashCode();
        out.writeLong(val);
        put(val, m_object);
    }

    private void readFromParcel(final Parcel in) {
        final long val = in.readLong();
        m_object = get(val);
    }

/////

    private static final Map<Long, Object> s_sharableMap = new HashMap<Long, Object>(3);

    synchronized private static void put(long key, final Object obj) {
        s_sharableMap.put(key, obj);
    }

    synchronized private static Object get(long key) {
        return s_sharableMap.remove(key);
    }
}