
Task - CustomerManagentSystem System.
step 1 - create project on sping initilizer nd add dependency.
step 2 - import on intellije and setup it nd configure java17 to run java project.
step 3- create packeges and classes like packges are controller,service,serviceImpl,entity,mapper,auth,repository,dto etc.
step 4- confugure the database in application.properties.
step 5 - create restfull all api's for customer and orders.
step 6 - also create mapper interface for return dto . (securing out dataype, nd tranfer object).
step 7 - After create aauth package for configuration jwt token.
step 8 - create JwtTokenUtil class, inside generateToken methods, check token expiration, vality token etc. all connfiguration.
step 9 - create Login class for Admin and Customer.
step 10 - suppose create token usning theire RoleId Basis, ADMIN is RoleId = 1, and Customer RoleId = 2;
step 11 - Adming Login using contactNo and roleId(1) then generate token.
step 12 - Customer Login using contactNo and roleId(2) then generate token.
atep 13 - In token payload we saved/claims contact no and roleId.
step 14 - Authorization api's in controller, get roleId/admon or user from token and apply this roldId conditions.restion api's and secure api's using token also.
step 15 - same way all api's secure.
step 16 - each and every api's test.

Also there are many exception occured and also fixed it.

Here my Project Github Respository Link - https://github.com/sagarahire9545/customerManagementSysten/tree/master/

Here all Sccrenshots and my api's creation progress , In this document we also add the one column for who is used the api's like Admin Permission this APi. - https://docs.google.com/spreadsheets/d/1kERtUanUkImQD34Lv4xxssyPTiwsR7PJS7QVC6KgFDo/edit?usp=sharing

Thank You !!
