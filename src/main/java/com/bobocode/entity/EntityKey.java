package com.bobocode.entity;

public record EntityKey<T>(Class<T> type, Object id) {
}
