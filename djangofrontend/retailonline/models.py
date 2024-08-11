# This is an auto-generated Django model module.
# You'll have to do the following manually to clean this up:
#   * Rearrange models' order
#   * Make sure each model has one field with primary_key=True
#   * Make sure each ForeignKey and OneToOneField has `on_delete` set to the desired behavior
#   * Remove `managed = False` lines if you wish to allow Django to create, modify, and delete the table
# Feel free to rename the models, but don't rename db_table values or field names.
from django.db import models

class Orders(models.Model):
    ordernumber = models.AutoField(primary_key=True)
    customerid = models.ForeignKey('Users', models.DO_NOTHING, db_column='customerid')
    storeid = models.ForeignKey('Product', models.DO_NOTHING, db_column='storeid')
    productname = models.CharField(max_length=30)
    unitsordered = models.IntegerField()
    ordertime = models.DateTimeField()

    class Meta:
        db_table = 'orders'


class Product(models.Model):
    storeid = models.OneToOneField('Store', models.DO_NOTHING, db_column='storeid', primary_key=True)
    productname = models.CharField(max_length=30)
    numberofunits = models.IntegerField()
    priceperunit = models.FloatField()

    class Meta:
        db_table = 'product'
        unique_together = (('storeid', 'productname'),)


class Productsupplyrequests(models.Model):
    requestnumber = models.AutoField(primary_key=True)
    managerid = models.ForeignKey('Users', models.DO_NOTHING, db_column='managerid')
    warehouseid = models.ForeignKey('Warehouse', models.DO_NOTHING, db_column='warehouseid')
    storeid = models.ForeignKey(Product, models.DO_NOTHING, db_column='storeid')
    productname = models.CharField(max_length=30)
    unitsrequested = models.IntegerField()

    class Meta:
        db_table = 'productsupplyrequests'


class Productupdates(models.Model):
    updatenumber = models.AutoField(primary_key=True)
    managerid = models.ForeignKey('Users', models.DO_NOTHING, db_column='managerid')
    storeid = models.ForeignKey(Product, models.DO_NOTHING, db_column='storeid')
    productname = models.CharField(max_length=30)
    updatedon = models.DateTimeField()

    class Meta:
        db_table = 'productupdates'


class Store(models.Model):
    storeid = models.IntegerField(primary_key=True)
    name = models.CharField(max_length=30)
    latitude = models.DecimalField(max_digits=8, decimal_places=6)
    longitude = models.DecimalField(max_digits=9, decimal_places=6)
    managerid = models.ForeignKey('Users', models.DO_NOTHING, db_column='managerid')
    dateestablished = models.DateField(blank=True, null=True)

    class Meta:
        db_table = 'store'


class Users(models.Model):
    userid = models.AutoField(primary_key=True)
    name = models.CharField(max_length=50)
    password = models.CharField(max_length=11)
    latitude = models.DecimalField(max_digits=8, decimal_places=6)
    longitude = models.DecimalField(max_digits=9, decimal_places=6)
    type = models.CharField(max_length=10)

    class Meta:
        db_table = 'users'


class Warehouse(models.Model):
    warehouseid = models.IntegerField(primary_key=True)
    area = models.IntegerField(blank=True, null=True)
    latitude = models.DecimalField(max_digits=8, decimal_places=6)
    longitude = models.DecimalField(max_digits=9, decimal_places=6)

    class Meta:
        db_table = 'warehouse'
