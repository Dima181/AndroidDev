package ru.mirea.hristov.employeedb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Employee {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name; // Имя супергероя
    public String alias; // Псевдоним супергероя
    public String superPower; // Суперспособности
}