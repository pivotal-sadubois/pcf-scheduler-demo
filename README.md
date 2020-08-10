# Scheduler with Pivotal Cloud Foundry

This project contains 2 modules to deploy on Cloud Foundry. The [spring-scheduled](./spring-scheduled) module uses the `@Scheduled` annotation, and the [pcf-scheduler](./pcf-scheduler) module uses the PCF Scheduler service. 

## Usage

### Compile the project

Run the following command to build the 2 modules and to generate jars ready to deploy.

    mvn clean package

### Add PCF Scheduler service

Before deploying, you have to create a new `Scheduler for PCF` service available from the Marketplace.

    cf create-service scheduler-for-pcf standard pcf-scheduler  


### Deploying both module

You can deploy the each modules manually or use the provided `manifest.yml` deployment file to deploy both of them. The `pcf-scheduler` app will bind to the newly `pcf-scheduler` service. Great names right? ;)

    cf push
  
  
## Spring @Scheduled

The code runs great and every minutes as expected. But once you have more than one instance (and you should), each instances are executing the scheduled task

### Logs example from @Scheduled

```
    2018-02-28T10:58:00.00-0500 [APP/PROC/WEB/0] OUT IP Address: 515d00e6-def4-4d08-6745-7bf9/10.255.22.200, Thread: pool-1-thread-1, Time: 15:58:00.001
    2018-02-28T10:58:00.00-0500 [APP/PROC/WEB/1] OUT IP Address: 50c8060c-bb62-474a-69e8-7f43/10.251.58.164, Thread: pool-1-thread-1, Time: 15:58:00.001
    2018-02-28T10:59:00.00-0500 [APP/PROC/WEB/0] OUT IP Address: 515d00e6-def4-4d08-6745-7bf9/10.255.22.200, Thread: pool-1-thread-1, Time: 15:59:00.001
    2018-02-28T10:59:00.00-0500 [APP/PROC/WEB/1] OUT IP Address: 50c8060c-bb62-474a-69e8-7f43/10.251.58.164, Thread: pool-1-thread-1, Time: 15:59:00
    2018-02-28T11:00:00.00-0500 [APP/PROC/WEB/0] OUT IP Address: 515d00e6-def4-4d08-6745-7bf9/10.255.22.200, Thread: pool-1-thread-1, Time: 16:00:00.001
    2018-02-28T11:00:00.00-0500 [APP/PROC/WEB/1] OUT IP Address: 50c8060c-bb62-474a-69e8-7f43/10.251.58.164, Thread: pool-1-thread-1, Time: 16:00:00.001
```

The above sample has 2 running instances and you can notice in the logs that both of them are running the job. You can avoid that by using the `Scheduler for PCF` service in the next example.

## PCF Scheduler

Once the `pcf-scheduler` app is deployed, you can add a cron job to run the code every minute. You will need to install the CF cli plugin.

    cf create-job pcf-scheduler my-demo ".java-buildpack/open_jdk_jre/bin/java -cp ./BOOT-INF/classes ninja.spring.pcfscheduler.Scheduler"
    cf schedule-job my-demo "0/1 * ? * *"

The first command will create a job with the command to execute. Let me know if you find a more elegant way :)
The second command will associate a cron schedule to the previously created job. Notice the syntax? Somehow it is NOT a regular cron expression and there is no documentation about it (cron).

### Logs example from scheduler

```
   2018-03-01T11:58:00.72-0500 [CELL/0] OUT Creating container
   2018-03-01T11:58:01.48-0500 [CELL/0] OUT Successfully created container
   2018-03-01T11:58:06.24-0500 [APP/TASK/08e8c101-f36c-4d21-99e5-9976e7fc6855-|-051838d5-9010-44d5-8a79-8196276211e6/0] OUT IP Address: 91c6f6a4-0ba0-4940-9f86-a849e734328c/10.255.210.170, Thread: main, Time: 16:58:06.226
   2018-03-01T11:58:06.25-0500 [APP/TASK/08e8c101-f36c-4d21-99e5-9976e7fc6855-|-051838d5-9010-44d5-8a79-8196276211e6/0] OUT Exit status 0
   2018-03-01T11:58:06.27-0500 [CELL/0] OUT Stopping instance 91c6f6a4-0ba0-4940-9f86-a849e734328c
   2018-03-01T11:58:06.27-0500 [CELL/0] OUT Destroying container
   2018-03-01T11:58:07.03-0500 [CELL/0] OUT Successfully destroyed container
   2018-03-01T11:59:00.76-0500 [CELL/0] OUT Creating container
   2018-03-01T11:59:01.32-0500 [CELL/0] OUT Successfully created container
   2018-03-01T11:59:06.80-0500 [APP/TASK/08e8c101-f36c-4d21-99e5-9976e7fc6855-|-051838d5-9010-44d5-8a79-8196276211e6/0] OUT IP Address: 2033bbff-65c4-426e-a07a-ceaef072a8b5/10.249.187.45, Thread: main, Time: 16:59:06.793
   2018-03-01T11:59:06.81-0500 [APP/TASK/08e8c101-f36c-4d21-99e5-9976e7fc6855-|-051838d5-9010-44d5-8a79-8196276211e6/0] OUT Exit status 0
   2018-03-01T11:59:06.82-0500 [CELL/0] OUT Stopping instance 2033bbff-65c4-426e-a07a-ceaef072a8b5
   2018-03-01T11:59:06.82-0500 [CELL/0] OUT Destroying container
   2018-03-01T11:59:07.33-0500 [CELL/0] OUT Successfully destroyed container
```

Notice the creation of a new container that will execute the code only once every minute.

## More details on my blog
If you want to learn more, check out [my blog](https://medium.com/@christophef/scheduler-with-cloud-foundry-2f98d3daef35)

## Useful links

* [PCF Scheduler](http://docs.pivotal.io/pcf-scheduler/1-1/using.html)
* [CF CLI plugin](https://network.pivotal.io/products/p-scheduler-for-pcf)
* [PCF Scheduler APIs](http://docs.pivotal.io/pcf-scheduler/1-1/api/)
