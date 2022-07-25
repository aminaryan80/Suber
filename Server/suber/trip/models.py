from django.db import models
from django.utils import timezone

from account.models import Passenger, Driver


class PassengerTripHistory(models.Model):
    passenger = models.ForeignKey(Passenger, on_delete=models.CASCADE)
    driver = models.ForeignKey(Driver, on_delete=models.CASCADE)
    source = models.CharField(max_length=200)
    destination = models.CharField(max_length=200)
    date = models.DateTimeField(default=timezone.now)
    price = models.IntegerField()


class DriverTripHistory(models.Model):
    driver = models.ForeignKey(Driver, on_delete=models.CASCADE)
    source = models.CharField(max_length=200)
    destination = models.CharField(max_length=200)
    date = models.DateTimeField(default=timezone.now)
    price = models.IntegerField()


class OnGoingTrip(models.Model):
    passenger = models.ForeignKey(Passenger, on_delete=models.CASCADE)
    source = models.CharField(max_length=200)
    destination = models.CharField(max_length=200)
    price = models.IntegerField()
