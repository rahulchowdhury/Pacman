# Pacman
Pacman is a lightweight and easy to use **P**arallel **A**PI **C**alls **Man**ager ( _yeah, you might have guessed how the name came ;-)_ )

You might need to execute multiple API calls at once to speed up loading of your app and proceed only when you have the data from all the required API calls. Managing this using normal boolean flags or such methods can be a pain and can result to incorrect data or behaviour.

That is where Pacman comes to play, as you just set it up with some `CallGroup`, make the API calls and then update Pacman as and when you get a response from the call.

After all the calls are done processing, Pacman will fire a callback to an attached listener ( _which you have set up_ ) and then you have do whatever processing you needed to do after the calls were done.

## Adding Pacman to your project
Adding Pacman to your project is very simple, just copy the files `Pacman.java` and `CallGroup.java` and paste them under a same package in your project, and voila, you’re all set.

## Setting up
Getting started is real easy but must be done properly to ensure correct behaviour and proper callback.

Divide your API calls into groups, for example,

You need to run APIs **A**, **B**, **C** and **D** in parallel, however **C** and **D** more or less achieves the same thing, let’s say C uses User Id to fetch user’s Profile Photo and D uses User Id to fetch user’s profile information, so you can club them together into a single group, say **U**.

So, now you have 3 groups **A**, **B** and **U**, where U contains two API calls to be made in parallel. Now let’s create a list of `CallGroup` to inform Pacman about your API calls.

You now need to assign an unique identifier for each group for Pacman to correctly identify them. You can do that by creating some constants for each group like this,

```
public static final long GROUP_A = 72912;
public static final long GROUP_B = 16392;
public static final long GROUP_U = 93527;
```

Now that you have the constants created, you need to form the list of `CallGroup`,

```
List<CallGroup> callGroups = new ArrayList<>();
callGroups.add(new CallGroup(GROUP_A, 1));
callGroups.add(new CallGroup(GROUP_B, 1));
callGroups.add(new CallGroup(GROUP_U, 2));
```

The last thing you need to do to get set up is to initialize Pacman with the list of API calls that you just created,

```
Pacman.initialize(callGroups, new OnCallsCompleteListener() {
	@Override
	public void onCallsCompleted() {
		//All API calls are done, you can proceed with you next task here
	}
});
```

## Notifying Pacman of API call completion
Whenever you get a response from your API call you need to notify Pacman that the API call is done so that Pacman can register that event in its system. Now there can be two cases based on your API structure,

### Parallel or Independant API Calls in a Single Group
If you have a group U under which you are making 2 API calls which are independent of each other, as we assumed in the previous example while setting up Pacman, then you just need to call the following method on the completion of each API call,

`Pacman.postCallGroupUpdate(GROUP_U);`

### Serial or Dependant API Calls in a Single Group
If you have a group which might have more than 1 API calls depending on the response of the first API call, then you can do the following after the response of the first call,

`Pacman.postCallGroupUpdate(GROUP_ID, noOfCallsToAdd);`

where `noOfCallsToAdd` is the no of API calls you will be performing further in the group based on the response of the first call, and after the response of those individual API calls which doesn’t have any further dependencies, you need to call the usual update method,

`Pacman.postCallGroupUpdate(GROUP_ID);`


## License
```
Copyright 2016 Rahul Chowdhury

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
