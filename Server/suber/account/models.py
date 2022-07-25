from django.db import models


class Passenger(models.Model):
    username = models.CharField(max_length=200)
    password = models.CharField(max_length=200)
    first_name = models.CharField(max_length=200)
    last_name = models.CharField(max_length=200)
    balance = models.IntegerField(default=0)


class Driver(models.Model):
    username = models.CharField(max_length=200)
    password = models.CharField(max_length=200)
    first_name = models.CharField(max_length=200)
    last_name = models.CharField(max_length=200)
    car = models.CharField(max_length=200)
    balance = models.IntegerField(default=0)


class Transaction(models.Model):
    driver = models.ForeignKey(Driver, on_delete=models.CASCADE)
    card_number = models.CharField(max_length=16)
    amount = models.IntegerField()
