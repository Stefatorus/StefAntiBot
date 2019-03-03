# StefAntiBot - Mitigate Minecraft Layer 7 Attacks
Advanced way to mitigate bot attacks for large minecraft networks. This is a proof of concept for how this concept can be used to improve performance to improve performance.
It was optimized for performance and scalability.


## How To Install
After cloning the source and configuring to your own needs, make the firewall automatically run on system startup. The StefAntiBot plugin should be added to your bungeecord instances in order to further mitigate bots in case they managed to enter your server.

## How to create a ip whitelist?
I suggest using maravento's blackip program to automatically download a list of geo ipranges that you can further use to create a whitelist of your targeted areas. I also suggest optimizing the ip-ranges to add that extra speed to the firewall; considering it may have to work with tens of thousands of ip ranges per connection.
https://github.com/maravento/blackip

## Why better than past approaches?
Filtering the bots via an firewall that is additionaly controlled by the server allows for the same versatilty while keeping the load off your server. Not only that, but the way the system is implemented is currently superior to past iptables approaches which simply add a rule for each ip added and will clog up your iptables config.


## How does it fare in enterprise usecases?
The system was developed to be used on the Gamster.org minecraft community, with the following key aspects:
- Scalability
- Mitigation Speed
- Real User Impact
The scalability is great, because the system only requires each of your entry servers to use the firewall. Iptables & ipsets are really efficient and will work fine in large attacks.
The mitigation is almost instant. Once the connections/second are passed, people that aren't whitelisted instantly get blocked from joining. The attack can't surpass the configured ammount of connections.
Real Users that are already on the server will not get affected. Whitelisted players will still be able to join when the server is under attack. Additional measures can be implemented on top of the default system to allow safe players to still play.

## Do you have any visual representation on how this works?
Yes, i made one for people to have a easier time understanding the approach.
![Firewall Diagram](http://stefatorus.go.ro/img/GitHub/Layer_7_Minecraft_DDoS_Filter.png)
