package com.hysteria.practice.player.party.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PartyEvent {

	FFA("FFA"),
	SPLIT("Split"),
	REDROVER("Red Rover"),
	HCFClass("HCFClass");

	private final String name;

}
