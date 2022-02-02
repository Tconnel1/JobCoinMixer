package com.example.mixer.Repository;

import com.example.mixer.enums.Status;
import com.example.mixer.model.MixerRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MixerRepository {
    private final Map<String, MixerRecord> basicStorage = new HashMap<>();

    public void add(String depositAddress, MixerRecord mixerRecord) {
        basicStorage.put(depositAddress, mixerRecord);
    }

    public MixerRecord get(String depositAddress) {
        return basicStorage.get(depositAddress);
    }

    public void remove(String depositAddress) {
        basicStorage.remove(depositAddress);
    }

    public void update(String depositAddress, MixerRecord mixerRecord) {
        basicStorage.replace(depositAddress, mixerRecord);
    }

    public List<MixerRecord> getRecordsInProgress() {
        List<MixerRecord> records = new ArrayList<>();
        basicStorage.forEach((s, mixerRecord) -> {
            if (mixerRecord.getStatus().equals(Status.TRANSFERRING)) {
                records.add(mixerRecord);
            }
        });
        return records;
    }

    public List<MixerRecord> getRecordsAwaitingDeposit() {
        List<MixerRecord> records = new ArrayList<>();
        basicStorage.forEach((s, mixerRecord) -> {
            if (mixerRecord.getStatus().equals(Status.AWAITING_DEPOSIT)) {
                records.add(mixerRecord);
            }
        });
        return records;
    }
}
