ipset=/sbin/ipset
iptables=/sbin/iptables
route=/etc/acl
zone=/etc/zones

# What port should we protect against bots?
port=666

# This are the settings for the firewall. You can change this 
# to account to your playerbase. This is a must!
limit=300

# Limit max persec conns.
seconds=5
maxper=5

# Limit new connections per second. This is highly useful when
# somebody starts an attack because it basically makes his attack
# literally have no visual effect.
#
# Don't rely solely on this as this doesn't really fix the issue,
# just get rid of suspicious IP's.
burstconns=40
normalconns=15

# These settings work along with the plugin to block proxies
timeout=900


# This works by having whitelisted countries that will allways
# be able to connect to your server. Meanwhile, the people from
# other countries will have a limited ability to join.
#
# Useful commands:
# CLEAN WHITELIST: sudo ipset flush whitelist
# CLEAN PROXIES: sudo ipset flush proxies
#

# Create Whitelist IPSET

$ipset -F
$ipset -N -! proxies hash:net maxelem 150000 timeout $timeout
$ipset -N -! whitelist hash:net maxelem 1000000
 for ip in $(cat $zone/whitelist.zone); do
  $ipset -A whitelist $ip
 done
 
# Prepare BotManager System

# Step 1. Create & Flush Table
$iptables -N BotManager
$iptables -F BotManager

# Step 2. Check for the max connection amount to mitigate the attack.
$iptables -A BotManager -p tcp -m set --match-set proxies src -j DROP
$iptables -A BotManager -p tcp -m set --match-set whitelist src -j ACCEPT
$iptables -A BotManager -p tcp --syn --dport $port -m recent --update --seconds $seconds --hitcount $maxper -j DROP
$iptables -A BotManager -p tcp --syn --dport $port -m connlimit --connlimit-above $limit --connlimit-mask 0 -j DROP
$iptables -A BotManager -m state --state RELATED,ESTABLISHED -m limit --limit $normalconns/second --limit-burst $burstconns -j ACCEPT
$iptables -A BotManager -p tcp --dport $port -j DROP

# Add BotManager to iptables and remove it just in case it is already there.
$iptables -D INPUT -p tcp -j BotManager
$iptables -A INPUT -p tcp -j BotManager