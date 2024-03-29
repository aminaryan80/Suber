# Generated by Django 3.1.5 on 2022-07-25 10:58

from django.db import migrations, models
import django.db.models.deletion
import django.utils.timezone


class Migration(migrations.Migration):

    initial = True

    dependencies = [
        ('account', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='PassengerTripHistory',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('source', models.CharField(max_length=200)),
                ('destination', models.CharField(max_length=200)),
                ('date', models.DateTimeField(default=django.utils.timezone.now)),
                ('price', models.IntegerField()),
                ('driver', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='account.driver')),
                ('passenger', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='account.passenger')),
            ],
        ),
        migrations.CreateModel(
            name='OnGoingTrip',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('source', models.CharField(max_length=200)),
                ('destination', models.CharField(max_length=200)),
                ('price', models.IntegerField()),
                ('passenger', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='account.passenger')),
            ],
        ),
        migrations.CreateModel(
            name='DriverTripHistory',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('source', models.CharField(max_length=200)),
                ('destination', models.CharField(max_length=200)),
                ('date', models.DateTimeField(default=django.utils.timezone.now)),
                ('price', models.IntegerField()),
                ('driver', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='account.driver')),
            ],
        ),
    ]
