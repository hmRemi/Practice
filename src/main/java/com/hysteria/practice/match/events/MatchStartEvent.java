package com.hysteria.practice.match.events;

import com.hysteria.practice.utilities.event.CustomEvent;
import com.hysteria.practice.match.Match;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MatchStartEvent extends CustomEvent {

    private Match match;

}