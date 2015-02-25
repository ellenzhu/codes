# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations
import datetime
from django.conf import settings
import GamesApp.models


class Migration(migrations.Migration):

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
    ]

    operations = [
        migrations.CreateModel(
            name='Game',
            fields=[
                ('id', models.AutoField(verbose_name='ID', auto_created=True, primary_key=True, serialize=False)),
                ('created_on', models.DateTimeField(auto_now_add=True, default=datetime.datetime.now)),
                ('updated_on', models.DateTimeField(auto_now=True, default=datetime.datetime.now)),
                ('game_name', models.CharField(max_length=200, unique=True)),
                ('description', models.TextField()),
                ('image', models.ImageField(upload_to=GamesApp.models.get_upload_file_name, null=True, blank=True)),
                ('price', models.DecimalField(max_digits=10, decimal_places=2)),
                ('code', models.FileField(upload_to=GamesApp.models.get_upload_file_name, null=True, blank=True)),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='Players',
            fields=[
                ('id', models.AutoField(verbose_name='ID', auto_created=True, primary_key=True, serialize=False)),
                ('created_on', models.DateTimeField(auto_now_add=True, default=datetime.datetime.now)),
                ('updated_on', models.DateTimeField(auto_now=True, default=datetime.datetime.now)),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='Developer',
            fields=[
                ('players_ptr', models.OneToOneField(auto_created=True, serialize=False, primary_key=True, parent_link=True, to='GamesApp.Players')),
                ('developed_games', models.ManyToManyField(related_name='developers', to='GamesApp.Game')),
            ],
            options={
            },
            bases=('GamesApp.players',),
        ),
        migrations.CreateModel(
            name='Score',
            fields=[
                ('id', models.AutoField(verbose_name='ID', auto_created=True, primary_key=True, serialize=False)),
                ('score', models.IntegerField(max_length=10)),
                ('game', models.ForeignKey(related_name='game–scores', to='GamesApp.Game')),
                ('name', models.ForeignKey(related_name='playerscores', to='GamesApp.Players')),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.AddField(
            model_name='players',
            name='game',
            field=models.ManyToManyField(related_name='players', to='GamesApp.Game'),
            preserve_default=True,
        ),
        migrations.AddField(
            model_name='players',
            name='user',
            field=models.OneToOneField(to=settings.AUTH_USER_MODEL),
            preserve_default=True,
        ),
    ]
