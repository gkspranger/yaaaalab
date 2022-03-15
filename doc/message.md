```clojure
{
  :text "!help me"
  :user "user"
  :source "shell"
  :channel "default"
  :message-responder #'yaaaalab.adapters.shell/reply
  :message-sender #'yaaaalab.adapters.shell/send
  :event-emitter #'yaaaalab.event/emit
  :view-renderer #'yaaaalab.view/render
}
```
