from django.db import models
from django.utils import timezone

from account.models import Passenger


class PassengerTripHistory(models.Model):
    user = models.ForeignKey(Passenger, on_delete=models.CASCADE)
    source = models.CharField(max_length=200)
    destination = models.CharField(max_length=200)
    date = models.DateTimeField(default=timezone.now)


class DriverTripHistory(models.Model):
    user = models.ForeignKey(Passenger, on_delete=models.CASCADE)
    source = models.CharField(max_length=200)
    destination = models.CharField(max_length=200)
    date = models.DateTimeField(default=timezone.now)


class OnGoingTrip(models.Model):
    passenger = models.ForeignKey(Passenger, on_delete=models.CASCADE)
    source = models.CharField(max_length=200)
    destination = models.CharField(max_length=200)
    price = models.IntegerField()
