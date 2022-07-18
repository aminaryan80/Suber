from django.db import models


class DiscountCode(models.Model):
    code = models.CharField(max_length=20)
    percent = models.IntegerField()
