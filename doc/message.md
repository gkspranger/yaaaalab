```clojure
{
  :text "!help me"
  :user "user"
  :source "shell"
  :channel "default"
  :response-dispatcher #'yaaaalab.adapters.shell/reply-to-command
  :message-dispatcher #'yaaaalab.adapters.shell/send-message
}
```