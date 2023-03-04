package com.hysteria.practice.player.profile.file;

import com.hysteria.practice.player.profile.Profile;

public interface IProfile {

    void save(Profile profile);

    void load(Profile profile);
}
