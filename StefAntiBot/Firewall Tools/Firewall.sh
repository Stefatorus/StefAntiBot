ipset=/sbin/ipset
iptables=/sbin/iptables
route=/etc/acl
zone=zones

wgetd="wget -q -c --retry-connrefused -t 0"

if [ ! -d $zone ]; then mkdir -p $zone; fi
		echo "Downloading country ip ranges..."
        $wgetd http://www.ipdeny.com/ipblocks/data/countries/all-zones.tar.gz && tar -C $zone -zxvf all-zones.tar.gz >/dev/null 2>&1 && rm -f all-zones.tar.gz >/dev/null 2>&1


# What port should we protect against bots?
port=25565

# Limit new connections per second. This is highly useful when
# somebody starts an attack because it basically makes his attack
# literally have no visual effect.
#
# Don't rely solely on this as this doesn't really fix the issue,
# just get rid of suspicious IP's.
burstconns=40

# TODO: Plugin whitelist
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


echo "Preparing ipset configuration"
$ipset -F
$ipset -N -! proxies hash:net maxelem 150000 timeout $timeout
$ipset -N -! whitelist hash:net maxelem 1000000
 # This is where you select which countries you want whitelisted. It's really important.
 for ip in $(cat $zone/{ro,md,fr,de,ie,gb,it,at,tr,bg,es}.zone); do
  $ipset -A whitelist $ip
 done
 
# Prepare BotManager System

# Step 1. Create & Flush Table
$iptables -N BotManager
$iptables -F BotManager

# Step 2. Check for the max connection amount to mitigate the attack.
$iptables -A BotManager -p tcp --dport $port -m set --match-set proxies src -j DROP
$iptables -A BotManager -p tcp --dport $port -m set --match-set whitelist src -j ACCEPT
$iptables -A BotManager -p tcp --dport $port --syn -m limit --limit $burstconns/s -j ACCEPT
$iptables -A BotManager -p tcp --dport $port --syn -j DROP

# Add BotManager to iptables and remove it just in case it is already there.
$iptables -D INPUT -p tcp -j BotManager
$iptables -A INPUT -p tcp -j BotManager
echo "Firewall applied successfully."
