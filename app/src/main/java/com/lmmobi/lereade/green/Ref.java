package com.lmmobi.lereade.green;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "references")
public class Ref implements Parcelable {
    @Id(autoincrement = true)
    private Long id;

    @NotNull
    @Property(nameInDb = "url")
    private String url;

    protected Ref(Parcel in) {
        id = in.readLong();
        url = in.readString();
    }

    @Generated(hash = 1251267913)
    public Ref(Long id, @NotNull String url) {
        this.id = id;
        this.url = url;
    }

    @Generated(hash = 831034445)
    public Ref() {
    }

    public static final Creator<Ref> CREATOR = new Creator<Ref>() {
        @Override
        public Ref createFromParcel(Parcel in) {
            return new Ref(in);
        }

        @Override
        public Ref[] newArray(int size) {
            return new Ref[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
  /*      if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }*/
        dest.writeLong(id);
        dest.writeString(url);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
