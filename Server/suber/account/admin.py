from django.contrib import admin

# Register your models here.
from account.models import Passenger, Driver

admin.site.register(Passenger)
admin.site.register(Driver)
