from django.contrib import admin

# Register your models here.
from account.models import Passenger, Driver, Transaction

admin.site.register(Passenger)
admin.site.register(Driver)
admin.site.register(Transaction)
