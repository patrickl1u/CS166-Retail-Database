# Generated by Django 4.1.3 on 2022-11-26 06:13

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Store',
            fields=[
                ('storeid', models.IntegerField(primary_key=True, serialize=False)),
                ('name', models.CharField(max_length=30)),
                ('latitude', models.DecimalField(decimal_places=6, max_digits=8)),
                ('longitude', models.DecimalField(decimal_places=6, max_digits=9)),
                ('dateestablished', models.DateField(blank=True, null=True)),
            ],
            options={
                'db_table': 'store',
            },
        ),
        migrations.CreateModel(
            name='Users',
            fields=[
                ('userid', models.AutoField(primary_key=True, serialize=False)),
                ('name', models.CharField(max_length=50)),
                ('password', models.CharField(max_length=11)),
                ('latitude', models.DecimalField(decimal_places=6, max_digits=8)),
                ('longitude', models.DecimalField(decimal_places=6, max_digits=9)),
                ('type', models.CharField(max_length=10)),
            ],
            options={
                'db_table': 'users',
            },
        ),
        migrations.CreateModel(
            name='Warehouse',
            fields=[
                ('warehouseid', models.IntegerField(primary_key=True, serialize=False)),
                ('area', models.IntegerField(blank=True, null=True)),
                ('latitude', models.DecimalField(decimal_places=6, max_digits=8)),
                ('longitude', models.DecimalField(decimal_places=6, max_digits=9)),
            ],
            options={
                'db_table': 'warehouse',
            },
        ),
        migrations.CreateModel(
            name='Product',
            fields=[
                ('storeid', models.OneToOneField(db_column='storeid', on_delete=django.db.models.deletion.DO_NOTHING, primary_key=True, serialize=False, to='retailonline.store')),
                ('productname', models.CharField(max_length=30)),
                ('numberofunits', models.IntegerField()),
                ('priceperunit', models.FloatField()),
            ],
            options={
                'db_table': 'product',
                'unique_together': {('storeid', 'productname')},
            },
        ),
        migrations.AddField(
            model_name='store',
            name='managerid',
            field=models.ForeignKey(db_column='managerid', on_delete=django.db.models.deletion.DO_NOTHING, to='retailonline.users'),
        ),
        migrations.CreateModel(
            name='Productupdates',
            fields=[
                ('updatenumber', models.AutoField(primary_key=True, serialize=False)),
                ('productname', models.CharField(max_length=30)),
                ('updatedon', models.DateTimeField()),
                ('managerid', models.ForeignKey(db_column='managerid', on_delete=django.db.models.deletion.DO_NOTHING, to='retailonline.users')),
                ('storeid', models.ForeignKey(db_column='storeid', on_delete=django.db.models.deletion.DO_NOTHING, to='retailonline.product')),
            ],
            options={
                'db_table': 'productupdates',
            },
        ),
        migrations.CreateModel(
            name='Productsupplyrequests',
            fields=[
                ('requestnumber', models.AutoField(primary_key=True, serialize=False)),
                ('productname', models.CharField(max_length=30)),
                ('unitsrequested', models.IntegerField()),
                ('managerid', models.ForeignKey(db_column='managerid', on_delete=django.db.models.deletion.DO_NOTHING, to='retailonline.users')),
                ('warehouseid', models.ForeignKey(db_column='warehouseid', on_delete=django.db.models.deletion.DO_NOTHING, to='retailonline.warehouse')),
                ('storeid', models.ForeignKey(db_column='storeid', on_delete=django.db.models.deletion.DO_NOTHING, to='retailonline.product')),
            ],
            options={
                'db_table': 'productsupplyrequests',
            },
        ),
        migrations.CreateModel(
            name='Orders',
            fields=[
                ('ordernumber', models.AutoField(primary_key=True, serialize=False)),
                ('productname', models.CharField(max_length=30)),
                ('unitsordered', models.IntegerField()),
                ('ordertime', models.DateTimeField()),
                ('customerid', models.ForeignKey(db_column='customerid', on_delete=django.db.models.deletion.DO_NOTHING, to='retailonline.users')),
                ('storeid', models.ForeignKey(db_column='storeid', on_delete=django.db.models.deletion.DO_NOTHING, to='retailonline.product')),
            ],
            options={
                'db_table': 'orders',
            },
        ),
    ]
